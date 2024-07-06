package rs.raf.catapult.breeds.photos.gallery

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import rs.raf.catapult.breeds.repository.BreedRepository
import rs.raf.catapult.breeds.db.Photo
import rs.raf.catapult.breeds.photos.gallery.PhotoGalleryContract.PhotoGalleryUiState
import rs.raf.catapult.breeds.photos.gallery.model.PhotoUiModel
import javax.inject.Inject

@HiltViewModel
class PhotoGalleryViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val breedRepository: BreedRepository,
) : ViewModel() {

    private val breedId: String = savedStateHandle.get<String>("breedId")!!
    private val viewModelPhotoId: String = savedStateHandle.get<String>("photoId")!!

    private val _state = MutableStateFlow(PhotoGalleryUiState())
    val state = _state.asStateFlow()
    private fun setState(reducer: PhotoGalleryUiState.() -> PhotoGalleryUiState) =
        _state.update(reducer)

    init {
        setState { copy(photoId = viewModelPhotoId) }
        observePhotos()
    }

    private fun observePhotos() {
        viewModelScope.launch {
            breedRepository.observeBreedPhotos(breedId = breedId)
                .distinctUntilChanged()
                .collect {
                    setState { copy(photos = it.map { it.asPhotoUiModel() }) }
                }
        }
    }

    private fun Photo.asPhotoUiModel() = PhotoUiModel(
        photoId = this.id,
        url = this.url,
    )
}
