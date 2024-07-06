package rs.raf.catapult.breeds.mappers

import rs.raf.catapult.breeds.api.model.BreedImageApiModel
import rs.raf.catapult.breeds.api.model.BreedsApiModel
import rs.raf.catapult.breeds.db.Breed
import rs.raf.catapult.breeds.db.BreedWithTemperaments
import rs.raf.catapult.breeds.db.Temperament
import rs.raf.catapult.breeds.db.Photo

fun BreedsApiModel.asBreedDbModel(): BreedWithTemperaments {


    val breed = Breed(
        id = this.id,
        this.name,
        this.description,
        altNames = this.altNames,
       // temperament = this.temperament.split(",").map { Temperament(it.trim().lowercase()) },
        origin = this.origin,
        wikipediaUrl = this.wikipediaUrl,
        lifeSpan = this.lifeSpan,
        adaptability = this.adaptability,
        affectionLevel = this.affectionLevel,
        childFriendly = this.childFriendly,
        dogFriendly = this.dogFriendly,
        healthIssues = this.healthIssues,
        sheddingLevel = this.sheddingLevel,
        socialNeeds = this.socialNeeds,
        rare = this.rare,
        avgWeightImp = this.weight.imperial.split(" - ").map { it.toDouble() }.toDoubleArray().average(),
        avgWeightMet = this.weight.metric.split(" - ").map { it.toDouble() }.toDoubleArray().average()
    )

    val temperaments = this.temperament.split(",").map { Temperament(it.trim().lowercase()) }

    return BreedWithTemperaments(breed, temperaments)

}

fun BreedImageApiModel.asBreedPhotoDbModel(breedId: String): Photo {
    return Photo(
        id = this.id,
        breedId = breedId,
        url = this.url
    )
}