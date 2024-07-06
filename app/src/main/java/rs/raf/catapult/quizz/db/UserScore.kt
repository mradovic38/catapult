package rs.raf.catapult.quizz.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserScore (
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    val score: Double,
    val isPublic: Boolean
)