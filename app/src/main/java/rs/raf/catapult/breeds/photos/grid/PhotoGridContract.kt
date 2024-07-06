package rs.raf.catapult.breeds.photos.grid

import rs.raf.catapult.breeds.details.model.BreedDetailsUiModel
import rs.raf.catapult.breeds.photos.grid.model.BreedPhotoUiModel
import rs.raf.catapult.breeds.photos.grid.model.PhotoUiModel

interface PhotoGridContract {
    data class PhotoGridUiState(
        val updating: Boolean = false,
        val gettingBreed: Boolean = false,
        val photos: List<PhotoUiModel> = emptyList(),
        val error: String = "",
        val breed: BreedPhotoUiModel? = null
    )
}