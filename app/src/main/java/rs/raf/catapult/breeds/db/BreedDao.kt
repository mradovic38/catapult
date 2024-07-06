package rs.raf.catapult.breeds.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface BreedDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertBreed(breed: Breed)

    @Transaction
    fun insert(breedWithTemperaments: BreedWithTemperaments){
        insertBreed(breedWithTemperaments.breed)
        breedWithTemperaments.temperaments.forEach { temperament ->
            insertTemperament(temperament)
        }
        insertAllTemperaments(breedWithTemperaments.temperaments.map { BreedTemperament(breedWithTemperaments.breed.id, it.name) })

    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAllTemperaments(temperaments: List<BreedTemperament>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertTemperament(temperament: Temperament)

    @Transaction
    fun insertAll(breeds: List<BreedWithTemperaments>) {
        breeds.forEach { breedWithTemperaments ->
            insertBreed(breedWithTemperaments.breed)
            breedWithTemperaments.temperaments.forEach { temperament ->
                insertTemperament(temperament)
            }
            insertAllTemperaments(breedWithTemperaments.temperaments.map { BreedTemperament(breedWithTemperaments.breed.id, it.name) })
        }
    }

    @Transaction
    @Query("SELECT * FROM Breed")
    fun getAll(): List<BreedWithTemperaments>

    @Transaction
    @Query("SELECT * FROM Breed")
    fun observeAll(): Flow<List<BreedWithTemperaments>>

    @Transaction
    @Query("SELECT * FROM Breed WHERE id = :breedId")
    fun observeBreed(breedId: String): Flow<BreedWithPhotos>

    @Transaction
    @Query("SELECT * FROM Breed WHERE id = :breedId")
    fun getBreedWithPhotos(breedId: String): BreedWithPhotos

    @Transaction
    @Query("SELECT * FROM Breed WHERE id = :id")
    fun getById(id: String): BreedWithTemperaments

    @Transaction
    @Query("SELECT * FROM Breed WHERE name LIKE :query")
    fun searchBreeds(query: String): List<BreedWithTemperaments>

    @Transaction
    @Query("SELECT * FROM Breed WHERE id NOT IN (:blacklist) ORDER BY RANDOM() LIMIT 1")
    fun getRandomBreed(blacklist: List<String>): BreedWithTemperaments

    @Query("SELECT * FROM Temperament WHERE name NOT IN (:blacklist) ORDER BY RANDOM() LIMIT 1")
    fun getRandomTemperament(blacklist: List<String>): Temperament

}