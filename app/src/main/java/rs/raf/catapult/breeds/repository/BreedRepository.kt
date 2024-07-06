package rs.raf.catapult.breeds.repository

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import retrofit2.HttpException
import rs.raf.catapult.breeds.api.BreedsApi
import rs.raf.catapult.breeds.db.BreedWithPhotos
import rs.raf.catapult.breeds.db.BreedWithTemperaments
import rs.raf.catapult.breeds.db.Photo
import rs.raf.catapult.breeds.db.Temperament
import rs.raf.catapult.breeds.mappers.asBreedDbModel
import rs.raf.catapult.breeds.mappers.asPhotoDbModel
import rs.raf.catapult.db.AppDatabase
import javax.inject.Inject

class BreedRepository @Inject constructor(
    private val breedsApi: BreedsApi,
    private val database: AppDatabase
){
//    private val database by lazy { CatapultDatabase.database }
    // lazy znaci da dohvata tek kad se prvi put trazi -- linija izbrisana nakon DI

//    private val breedsApi: BreedsApi by lazy { retrofit.create(BreedsApi::class.java) }
    // -- linija izbrisana nakon DI



    suspend fun fetchAllBreeds(){
        val breeds = breedsApi.getAllBreeds()
        database.breedDao().insertAll(breeds.map{it.asBreedDbModel()})
    }

    suspend fun fetchBreedAndPhotos(id: String){
        val breed = breedsApi.getBreed(id)
        val photos = fetchAllPhotosOfBreed(breedId = breed.id)
        database.breedDao().insert(breed.asBreedDbModel())
        database.photoDao().insertAllPhotos(photos)
    }


    fun getAllBreeds(): List<BreedWithTemperaments> {
        return database.breedDao().getAll();
    }

    fun observeAllBreeds(): Flow<List<BreedWithTemperaments>> {
        return database.breedDao().observeAll()
    }

    suspend fun observeBreed(id: String): Flow<BreedWithPhotos> {
        return database.breedDao().observeBreed(breedId = id)
    }

    fun getBreedById(id: String): BreedWithTemperaments {
        return database.breedDao().getById(id)
    }

    suspend fun getBreedWithPhotos(breedId: String): BreedWithPhotos {
        return database.breedDao().getBreedWithPhotos(breedId = breedId)
    }

    fun searchBreeds(query: String): List<BreedWithTemperaments> {
        return database.breedDao().searchBreeds("%$query%")
    }

    fun getRandomBreed(blacklist: List<String>): BreedWithTemperaments {
        return database.breedDao().getRandomBreed(blacklist)
    }



    fun getRandomTemperament(blacklist: List<String>): Temperament {
        return database.breedDao().getRandomTemperament(blacklist)
    }


    private suspend fun fetchBreedPhotos(breedId: String, limit:Int, page:Int): List<Photo>{
        val allPhotos = breedsApi.getBreedImages(breedId = breedId, limit = limit, page = page)
            .map { it.asPhotoDbModel(breedId) }
            .toMutableList()

        database.photoDao().insertAllPhotos(data = allPhotos)

        return allPhotos
    }

    suspend fun fetchAllPhotosOfBreed(breedId: String): List<Photo> {
        val images = database.photoDao().getAllPhotosOfBreed(breedId)
        if (images.isNotEmpty()) {
            return images
        }
        val photos = mutableListOf<Photo>()
        var page = 0
        var retries = 0

        while (true) {
            try {
                val newPhotos = fetchBreedPhotos(breedId = breedId, limit = 10, page = page)
                photos.addAll(newPhotos)
                if (newPhotos.size < 10) {
                    break
                }
                page++
                retries = 0
            } catch (e: HttpException) {
                if (e.code() == 429) {
                    retries++
                    if (retries > MAX_RETRIES) {
                        throw Exception("Rate limit prekoracen: $breedId nakon $retries pokusaja.")
                    }
                    delay(retries * BACKOFF_DELAY)
                } else {
                    throw e
                }
            } catch (e: Exception) {
                throw e
            }
        }

        return photos
    }

    companion object {
        private const val MAX_RETRIES = 10 // do 10 puta pokusava nakon rate limit prekoracenja
        private const val BACKOFF_DELAY = 1000L // da se smiri na 1s * max_retries
    }

    fun observeBreedPhotos(breedId: String) = database.photoDao().observeBreedImages(breedId = breedId)

    fun getRandomPhotoOfBreed(breedId: String, blacklistIds: List<String> ): Photo {
        return database.photoDao().getRandomPhotoOfBreed(breedId = breedId, blacklistIds= blacklistIds)
    }


}