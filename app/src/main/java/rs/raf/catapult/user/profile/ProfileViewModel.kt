package rs.raf.catapult.user.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import rs.raf.catapult.leaderboard.db.UserScore
import rs.raf.catapult.leaderboard.repository.LeaderboardRepository
import rs.raf.catapult.user.datastore.UserStore
import rs.raf.catapult.user.datastore.model.UserModel
import rs.raf.catapult.user.profile.model.ResultUiModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel@Inject constructor(
    private val userStore: UserStore,
    private val leaderboardRepository: LeaderboardRepository
): ViewModel() {

    private val events = MutableSharedFlow<ProfileUiEvent>()
    private val _state = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state

    fun setEvent(event: ProfileUiEvent) = viewModelScope.launch { events.emit(event) }

    private fun setState(reducer: ProfileState.() -> ProfileState) = _state.update(reducer)

    init {
        loadUserData()
        fetchBestScore()
        observeResults()
        observeEvents()
    }

    private fun observeResults(){
        viewModelScope.launch {
            setState { copy(fetching = true) } // evidencija inicijalnog ucitavanja podataka
            leaderboardRepository.observeUserScores() // ovo se desava svaki put kad se izmeni, cak i ako nije potrebno na frontu
                .distinctUntilChanged() // ovo to popravlja - tek ako primetis da ima razlike uradi recomposition i to
                .collect {
                    setState {
                        copy(
                            fetching = false,
                            allResults = it.map { it.asUserResultUiModel()}
                        )
                    }
                }
        }
    }

    private fun fetchBestScore(){
        viewModelScope.launch {
            try {
                val leaderboard =
                    withContext(Dispatchers.IO){leaderboardRepository.fetchLeaderboard(1)
                        }
                val nickname = state.value.nickname
                val bestScore = leaderboard.withIndex().filter { it.value.nickname == nickname }
                    .maxByOrNull { it.value.result }
                val bestScoreIndex = bestScore?.index ?: -1
                if (bestScore != null) {
                    setState { copy(bestRanking = bestScoreIndex, bestScore = bestScore.value.result )}
                }
            } catch (e: Exception) {
                setState { copy(resutsError = e.message ?: "An unknown error occurred.") }
            }
        }
    }


    private fun loadUserData() = viewModelScope.launch {
        val user = userStore.userData.value // DATA STORE SE AUTOMATSKI STAVLJA NA Dispatchers.IO: https://developer.android.com/codelabs/android-preferences-datastore#3
        Log.d("PROFILE" , user.toString());
        setState { copy(
            email = user.email,
            nickname = user.nickname,
            firstName = user.firstName,
            lastName =  user.lastName,

        ) }
    }

    private fun observeEvents() = viewModelScope.launch {
        events.collect { event ->
            when (event) {
                is ProfileUiEvent.OnEditClick -> {
                    loadUserData()
                    setState { copy(editMode=!editMode, error = "") }
                }
                is ProfileUiEvent.OnConfirmClick -> {
                    val (email, nickname, firstName, lastName) = event
                    try {
                        val user = UserModel(email, nickname, firstName, lastName)
                        userStore.updateUser(user)
                        setState {
                            copy(
                                email = userStore.userData.value.email,
                                nickname = userStore.userData.value.nickname,
                                firstName = userStore.userData.value.firstName,
                                lastName = userStore.userData.value.lastName,
                                error = "",
                                editMode = false,
                                allResults = emptyList(),
                                bestScore = 0f,
                                bestRanking = 0,
                                resutsError = ""
                            )
                        }
                        fetchBestScore()
                        observeResults()
                    } catch (e: IllegalArgumentException) {
                       setState { copy(
                           error = e.message ?: "An unknown error occurred.") }
                    }
                }
            }
        }
    }


    private fun UserScore.asUserResultUiModel() = ResultUiModel(
        id = this.id,
        score = this.score,
        isPublic = this.isPublic
    )


}