package rs.raf.catapult.breeds.api

import rs.raf.catapult.breeds.api.model.BreedImageApiModel
import rs.raf.catapult.breeds.api.model.BreedsApiModel
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BreedsApi {

    @GET("breeds")
    suspend fun getAllBreeds(

    ): List<BreedsApiModel>

    @GET("breeds/{id}")
    suspend fun getBreed(
        @Path("id") breedId: String,
    ): BreedsApiModel

    @GET("breeds/search")
    suspend fun searchBreeds(
        @Query("q") breedName: String,
    ): List<BreedsApiModel>

    @GET("images/search")
    suspend fun getBreedImages(
        @Query("order") order: String = "DESC",
        @Query("breed_ids") breedId: String,
        @Query("limit") limit: Int = 10,
        @Query("page") page: Int = 1,
    ): List<BreedImageApiModel>

//    @GET("users/{id}/albums")
//    suspend fun getUserAlbums(
//        @Path("id") userId: Int,
//    ): List<AlbumApiModel>

//    @DELETE("users/{id}")
//    suspend fun deleteUser()
}
