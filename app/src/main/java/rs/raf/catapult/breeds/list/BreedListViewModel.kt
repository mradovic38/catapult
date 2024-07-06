package rs.raf.catapult.breeds.list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import rs.raf.catapult.breeds.db.BreedWithTemperaments
import rs.raf.catapult.breeds.list.model.BreedListElementUiModel
import rs.raf.catapult.breeds.repository.BreedRepository
import javax.inject.Inject

@HiltViewModel // potrebno da bi koristili u screen
class BreedListViewModel @Inject constructor( // injection sa konstruktorom
    saveStateHandle: SavedStateHandle, // cuva stanje vezano za ViewModel
    private val repository: BreedRepository
): ViewModel() {

    private val events = MutableSharedFlow<ListUiEvent>()
    fun setEvent(event: ListUiEvent) = viewModelScope.launch { events.emit(event) }

    private val _state = MutableStateFlow(BreedListState()) // privatni state koji menjamo
    val state = _state.asStateFlow() // read-only javni state (da se ne bi menjalo sa view-a)
    private fun setState(reducer: BreedListState.() -> BreedListState) = _state.update(reducer)


    init { // sta raditi prilikom inicijalizacije
        fetchAllBreeds()
        observeBreeds()
        observeEvents()
    }

    private fun observeEvents() {
        viewModelScope.launch {
            events.collect {
                when (it) {
                    ListUiEvent.CloseSearchMode -> setState {
                        copy(isSearchMode = false) }

                    is ListUiEvent.SearchQueryChanged -> {
                        setState { copy(query = it.query) }
                    }

                    is ListUiEvent.SearchClicked -> {
                        searchClicked(query = it.query)
                    }

                    is ListUiEvent.ChangeSearchBarActive -> {
                        setState {
                            copy(isSearchMode = it.status) }
                    }

                }
            }
        }
    }


    private fun fetchAllBreeds() {
        viewModelScope.launch {
            setState { copy(fetching = true) } // state postaje fetching - ovo je vise kao updating sada
            try {
                withContext(Dispatchers.IO) {
                    repository.fetchAllBreeds()
                }
            } catch (error: Exception) {
               // setState { copy(error = BreedListState.ListError.ListUpdateFailed(cause = error)) }
            } finally {
                setState { copy(fetching = false) }
            }
        }
    }

    private fun searchClicked(query: String) {
        viewModelScope.launch {
            setState { copy(fetching = true) } // state postaje fetching
            try {
                val breeds = withContext(Dispatchers.IO) {
                    if(query.isNotEmpty())
                        repository.searchBreeds(query)
                    else
                        repository.getAllBreeds()

                }
                setState { copy(breeds = breeds.map { it.asBreedListElementUiModel() }) }
            } catch (error: Exception) {
                setState { copy(error = BreedListState.ListError.ListUpdateFailed(cause = error)) }

            } finally {
                setState { copy(fetching = false) }
            }
        }
    }

    // uzima iz baze i stavlja u state, ali osluskuje i promene
    private fun observeBreeds() {
        viewModelScope.launch {
            setState { copy(initialLoading = true) } // evidencija inicijalnog ucitavanja podataka
            repository.observeAllBreeds() // ovo se desava svaki put kad se izmeni, cak i ako nije potrebno na frontu
                .distinctUntilChanged() // ovo to popravlja - tek ako primetis da ima razlike uradi recomposition i to
                .collect {
                    setState {
                        copy(
                            initialLoading = false,
                            breeds = it.map { it.asBreedListElementUiModel()}
                        )
                    }
                }
        }
    }

    // konvertuje iz database u ui model
    private fun BreedWithTemperaments.asBreedListElementUiModel() = BreedListElementUiModel(
        id = this.breed.id,
        name = this.breed.name,
        altNames = this.breed.altNames,
        description = if (this.breed.description.length > 250) {
            this.breed.description.substring(0, 250) + "..."
        } else {
            this.breed.description
        },
        temperament = this.temperaments.map{it.name}.shuffled().take(3)
    )







}