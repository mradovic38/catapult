package rs.raf.catapult.breeds.mappers

import rs.raf.catapult.breeds.api.model.BreedImageApiModel
import rs.raf.catapult.breeds.db.Photo

fun BreedImageApiModel.asPhotoDbModel(breedId: String): Photo {
    return Photo(
        id = this.id,
        url = this.url,
        breedId = breedId
    )

}