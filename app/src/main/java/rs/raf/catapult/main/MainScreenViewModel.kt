package rs.raf.catapult.main

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import rs.raf.catapult.breeds.list.BreedListState
import rs.raf.catapult.breeds.list.ListUiEvent
import rs.raf.catapult.breeds.repository.BreedRepository
import javax.inject.Inject

@HiltViewModel // potrebno da bi koristili u screen
class MainScreenViewModel @Inject constructor( // injection sa konstruktorom
    private val repository: BreedRepository
): ViewModel() {

    private val events = MutableSharedFlow<MainUiEvent>()
    fun setEvent(event: MainUiEvent) = viewModelScope.launch { events.emit(event) }

    private val _state = MutableStateFlow(MainScreenState()) // privatni state koji menjamo
    val state = _state.asStateFlow() // read-only javni state (da se ne bi menjalo sa view-a)
    private fun setState(reducer: MainScreenState.() -> MainScreenState) = _state.update(reducer)


    init { // sta raditi prilikom inicijalizacije
        fetchAllBreeds()
        observeEvents()
    }

    private fun observeEvents(){
        viewModelScope.launch {
            events.collect {
                when (it) {
                    MainUiEvent.RetryClicked -> {
                        fetchAllBreeds()
                    }
                }
            }
        }
    }

    private fun fetchAllBreeds() {
        viewModelScope.launch {
            setState { copy(fetching = true) }
            try {
                val breeds = withContext(Dispatchers.IO) {
                    repository.fetchAllBreeds()
                    repository.getAllBreeds()
                }

                val breedBatches = breeds.chunked(10)

                withContext(Dispatchers.IO) {
                    breedBatches.forEach { batch ->
                        val jobs = batch.map { breed ->
                            async {
                                repository.fetchAllPhotosOfBreed(breed.breed.id)
                            }
                        }
                        try {
                            jobs.awaitAll()
                        } catch (e: Exception) {
                            Log.e("BATCH_FETCH_ERROR", "Error kad se fetchovao batch: ${e.message}")
                            throw e
                        }
                    }
                }
            } catch (error: Exception) {
                setState { copy(error = MainScreenState.MainError.MainUpdateFailed(cause = error)) }
            } finally {
                setState { copy(fetching = false) }
            }
        }
    }
}
