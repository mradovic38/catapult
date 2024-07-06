package rs.raf.catapult.breeds.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Photo (
    @PrimaryKey val id: String,
    val url: String,
    val breedId: String
)