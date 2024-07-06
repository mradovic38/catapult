package rs.raf.catapult.quizz.play

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import rs.raf.catapult.breeds.repository.BreedRepository
import javax.inject.Inject

@HiltViewModel
class PlayViewModel @Inject constructor(
    private val repository: BreedRepository
) : ViewModel() {

    private val events = MutableSharedFlow<PlayUiEvent>()
    private val _state = MutableStateFlow(PlayState())
    val state: StateFlow<PlayState> = _state

    private fun setState(reducer: PlayState.() -> PlayState) = _state.update(reducer)
    fun setEvent(event: PlayUiEvent) = viewModelScope.launch { events.emit(event) }

    init {
        observeEvents()
    }

    private fun observeEvents() {
        viewModelScope.launch {
            events.collect { event ->
                when (event) {
                    PlayUiEvent.OnLeaderboardClick -> {
                    }

                    PlayUiEvent.OnPlayClick -> {
                        setState { copy(categoriesMode = true) }

                    }

                    PlayUiEvent.OnCategoriesClose -> {
                        setState { copy(categoriesMode = false) }
                    }

                    is PlayUiEvent.OnCategoryClick -> {
                        setState {
                            copy(categoriesMode = false)
                        }
                    }

                }
            }
        }
    }


}