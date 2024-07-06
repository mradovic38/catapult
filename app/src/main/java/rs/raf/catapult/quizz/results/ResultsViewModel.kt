package rs.raf.catapult.quizz.results

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import rs.raf.catapult.leaderboard.repository.LeaderboardRepository
import rs.raf.catapult.user.datastore.UserStore
import javax.inject.Inject

@HiltViewModel
class ResultsViewModel @Inject constructor(
    private val leaderboardRepository: LeaderboardRepository,
): ViewModel()  {

    private val _state = MutableStateFlow(ResultsState())
    val state: StateFlow<ResultsState> = _state
    private fun setState(reducer: ResultsState.() -> ResultsState) = _state.update(reducer)

    private val events = MutableSharedFlow<ResultsUiEvent>()
    fun setEvent(event: ResultsUiEvent) = viewModelScope.launch { events.emit(event) }

    init{
        getLastUserScore()
        observeEvents()
    }

    private fun getLastUserScore() = viewModelScope.launch {
        withContext(Dispatchers.IO){
            leaderboardRepository.getLastUserScore().collect { userScore ->
                setState { copy(
                    score = userScore.score,
                    nickname = userScore.name,
                    scoreId = userScore.id
                )
                }
            }
        }
    }

    private fun observeEvents() = viewModelScope.launch {
        events.collect { event ->
            when (event) {
                is ResultsUiEvent.OnShareClick -> {
                        try{
                            withContext(Dispatchers.IO) {
                                leaderboardRepository.postUserScore(
                                    state.value.nickname,
                                    state.value.score,
                                    1
                                )

                                leaderboardRepository.putPublic(state.value.scoreId)
                            }
                            withContext(Dispatchers.Main) { // mora u main coroutine scope-u jer ovo moze samo u main
                                event.doRedirect()
                            }
                        }
                        catch (e: Exception){
                            setState { copy(error = e.message ?: "Unknown error")}
                        }

                    }

                }
            }
        }
}