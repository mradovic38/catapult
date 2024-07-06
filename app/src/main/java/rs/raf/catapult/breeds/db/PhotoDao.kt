package rs.raf.catapult.breeds.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface PhotoDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAllPhotos(data: List<Photo>)

    @Query("SELECT * FROM Photo where breedId = :breedId")
    fun observeBreedImages(breedId: String): Flow<List<Photo>> // flow mozemo da kolektujemo da vidimo sta se tu nalazi.

    @Query("SELECT * FROM Photo WHERE breedId = :breedId AND id NOT IN (:blacklistIds) ORDER BY RANDOM() LIMIT 1")
    fun getRandomPhotoOfBreed(breedId: String, blacklistIds: List<String>): Photo

    @Query("SELECT * FROM Photo WHERE breedId = :breedId")
    fun getAllPhotosOfBreed(breedId: String): List<Photo>

}