package rs.raf.catapult.breeds.photos.grid

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import rs.raf.catapult.breeds.db.Breed
import rs.raf.catapult.breeds.db.BreedWithTemperaments
import rs.raf.catapult.breeds.repository.BreedRepository
import rs.raf.catapult.breeds.db.Photo
import rs.raf.catapult.breeds.photos.grid.model.BreedPhotoUiModel
import rs.raf.catapult.breeds.photos.grid.model.PhotoUiModel
import javax.inject.Inject

@HiltViewModel
class PhotoGridViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: BreedRepository,
) : ViewModel() {

    private val breedId: String = savedStateHandle.get<String>("breedId")!!

    private val _state = MutableStateFlow(PhotoGridContract.PhotoGridUiState())
    val state = _state.asStateFlow()
    private fun setState(reducer: PhotoGridContract.PhotoGridUiState.() -> PhotoGridContract.PhotoGridUiState) = _state.update(reducer)


    init {
        fetchPhotos()
        observeBreedWithPhotos()
    }


    private fun observeBreedWithPhotos(){
        viewModelScope.launch {
            repository.observeBreed(breedId)
                .distinctUntilChanged()
                .collect {
                    setState { copy(breed = it.breed.asBreedPhotoUiModel(),
                        photos = it.photos.map{it.asPhotoUiModel()}) }
                }
        }
    }

    private fun fetchPhotos() {
        viewModelScope.launch {
            setState { copy(updating = true) }
            try {
                withContext(Dispatchers.IO) {
                    repository.fetchBreedAndPhotos(breedId)
                }
            } catch (error: Exception) {
                // setState {copy( error = error.message ?: "Failed fetching photos" )}
            }
            finally{
                setState { copy(updating = false) }
            }

        }
    }




    private fun Photo.asPhotoUiModel() = PhotoUiModel(
        id = this.id,
        photoUrl = this.url,
    )

    private fun BreedWithTemperaments.asBreedPhotoUiModel() = BreedPhotoUiModel(
        id = this.breed.id,
        name = this.breed.name,
    )
}