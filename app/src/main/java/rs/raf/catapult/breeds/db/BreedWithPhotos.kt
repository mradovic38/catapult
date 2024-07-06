package rs.raf.catapult.breeds.db

import androidx.room.Embedded
import androidx.room.Relation

data class BreedWithPhotos ( // za one-to-many vezu izmedju rase i slika rase
    @Embedded
    val breed: BreedWithTemperaments,

    @Relation(
        parentColumn = "id",
        entityColumn = "breedId" // id rase kao strani kljuc
    )
    val photos: List<Photo>
)