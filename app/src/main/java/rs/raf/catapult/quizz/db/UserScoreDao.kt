package rs.raf.catapult.quizz.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserScoreDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(score: UserScore)

    @Query("SELECT * FROM UserScore")
    fun getAll(): List<UserScore>
}