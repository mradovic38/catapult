package rs.raf.catapult.leaderboard.publicleaderboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import rs.raf.catapult.leaderboard.api.LeaderboardApi
import rs.raf.catapult.leaderboard.api.model.LeaderboardApiModel
import rs.raf.catapult.leaderboard.publicleaderboard.model.LeaderboardElementUiModel
import rs.raf.catapult.leaderboard.repository.LeaderboardRepository
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class LeaderboardViewModel @Inject constructor(
    private val leaderboardRepository: LeaderboardRepository
): ViewModel() {

    private val events = MutableSharedFlow<LeaderboardUiEvent>()
    fun setEvent(event: LeaderboardUiEvent) = viewModelScope.launch { events.emit(event) }

    private val _state = MutableStateFlow(LeaderboardState()) // privatni state koji menjamo
    val state = _state.asStateFlow() // read-only javni state (da se ne bi menjalo sa view-a)
    private fun setState(reducer: LeaderboardState.() -> LeaderboardState) = _state.update(reducer)

    init {
        fetchLeaderboard()
        observeEvents()
    }
    private fun fetchLeaderboard() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    setState {
                        copy(fetching = true)
                    }
                    val leaderboard = leaderboardRepository.fetchLeaderboard(1)
                    val totalQuizzesMap = leaderboard.groupBy { it.nickname }.mapValues { it.value.size }
                    setState {
                        copy(leaderboard = leaderboard.mapIndexed {index, it -> it.asLeaderboardUiModel(index + 1, totalQuizzesMap[it.nickname] ?: 0) })
                    }
                } catch (error: Exception) {
                    setState {
                        copy(error = LeaderboardState.ListError.ListUpdateFailed(cause = error))
                    }
                } finally {
                    setState {
                        copy(fetching = false)

                    }
                }
            }
        }
    }

    private fun observeEvents() {
        viewModelScope.launch {
            events.collect {
                when (it) {

                }
            }
        }
    }


    private fun LeaderboardApiModel.asLeaderboardUiModel(idx: Int, totalQuizzes: Int): LeaderboardElementUiModel {
        return LeaderboardElementUiModel(
            id = idx,
            nickname = this.nickname,
            createdAt = Date(this.createdAt),
            score = this.result.toDouble(),
            totalQuizzes = totalQuizzes
        )
    }
}

