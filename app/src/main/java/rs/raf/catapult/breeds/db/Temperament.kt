package rs.raf.catapult.breeds.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Temperament (
    @PrimaryKey var name: String = ""
) {
    init {
        name = name.lowercase()
    }
}