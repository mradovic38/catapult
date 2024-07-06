package rs.raf.catapult.breeds.db

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class BreedWithTemperaments (
    @Embedded
    val breed: Breed,

    @Relation(
        parentColumn = "id",
        entityColumn = "name",
        associateBy = Junction(BreedTemperament::class)
    )
    val temperaments: List<Temperament>
)