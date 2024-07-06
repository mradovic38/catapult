package rs.raf.catapult.breeds.photos.gallery

import rs.raf.catapult.breeds.details.model.BreedDetailsUiModel
import rs.raf.catapult.breeds.photos.gallery.model.PhotoUiModel

interface PhotoGalleryContract {
    data class PhotoGalleryUiState(
        val photos: List<PhotoUiModel> = emptyList(),
        val breed: BreedDetailsUiModel? = null,
        val photoId: String = "",
    )
}
