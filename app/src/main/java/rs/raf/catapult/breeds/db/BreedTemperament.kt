package rs.raf.catapult.breeds.db

import androidx.room.Entity


@Entity(primaryKeys = ["id", "name"])
data class BreedTemperament(
    val id: String,
    val name: String
)