package rs.raf.catapult.leaderboard.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserScoreDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insert(score: UserScore)

    @Query("SELECT * FROM UserScore")
    fun observeAll(): Flow<List<UserScore>>

    @Query("SELECT * FROM UserScore ORDER BY id DESC LIMIT 1")
    fun getLast(): Flow<UserScore>

    @Query("UPDATE UserScore SET isPublic = 1 WHERE id = :id")
    fun putPublic(id: Int)
}