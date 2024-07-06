package rs.raf.catapult.leaderboard.api

import kotlinx.serialization.Serializable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import rs.raf.catapult.leaderboard.api.model.LeaderboardApiModel
import rs.raf.catapult.leaderboard.api.model.LeaderboardApiModelResponse

interface LeaderboardApi {
//    GET leaderboard?category=Integer
//    Category query param je Integer vrednost kategorije.
//    Response je JsonArray svih rezultata (QuizResult) sortiranih od najboljeg do najlošijeg. Indeks u listi označava ranking rezultata, odnosno index 0 predstavlja 1. poziciju, index 1 predstavlja 2. poziciju, itd.

    @GET("leaderboard")
    suspend fun getLeaderboard(
        @Query("category") category: Int
    ): List<LeaderboardApiModel>


//    POST leaderboard
//    { "nickname": "rma", "result": 88.88, "category": 1 }
//    nickname: samo slova, brojevi i underscore:
//    result: vrednosti izmedju 0.00 i 100.00 su dozvoljene;
//    category: 1, 2, ili 3;
//    Response:
//    {
//        "result": { "category": 1, "nickname": "rma", "result": 88.88, "createdAt": 1717624105670 },
//        "ranking": 1
//    }


    @POST("leaderboard")
    suspend fun postLeaderboard(
        @Body leaderboardPostBody: LeaderboardPostBody
    ): LeaderboardApiModelResponse

    @Serializable
    data class LeaderboardPostBody(
        val nickname: String,
        val result: Float,
        val category: Int
    )

}