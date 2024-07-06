package rs.raf.catapult.breeds.details

import rs.raf.catapult.breeds.details.model.BreedDetailsUiModel
import rs.raf.catapult.breeds.details.model.BreedImageUiModel

data class BreedDetailsState(
    val breedId: String,
    val fetching: Boolean = false,
    val breed: BreedDetailsUiModel? = null,
    val error: DetailsError? = null,
    val images: List<BreedImageUiModel>? = null
) {
    sealed class DetailsError {
        data class DataUpdateFailed(val cause: Throwable? = null) : DetailsError()
    }
}
