package rs.raf.catapult.leaderboard.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserScore (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val score: Double,
    val isPublic: Boolean
)