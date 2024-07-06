package rs.raf.catapult.breeds.details.model

data class BreedWithPhotosUiModel (
    val breed:  BreedDetailsUiModel,
    val photos: List<BreedImageUiModel>
)