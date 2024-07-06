package rs.raf.catapult.leaderboard.repository

import kotlinx.coroutines.flow.Flow
import rs.raf.catapult.db.AppDatabase
import rs.raf.catapult.leaderboard.api.LeaderboardApi
import rs.raf.catapult.leaderboard.api.model.LeaderboardApiModel
import rs.raf.catapult.leaderboard.api.model.LeaderboardApiModelResponse
import rs.raf.catapult.leaderboard.db.UserScore
import javax.inject.Inject

class LeaderboardRepository @Inject constructor(
    private val leaderboardApi: LeaderboardApi,
    private val database: AppDatabase
){
    suspend fun fetchLeaderboard(category: Int): List<LeaderboardApiModel>{
        return leaderboardApi.getLeaderboard(category = category)
    }

    fun observeUserScores(): Flow<List<UserScore>> {
        return database.userScoreDao().observeAll();
    }

    fun putPublic(id: Int) {
        database.userScoreDao().putPublic(id);
    }

    fun getLastUserScore(): Flow<UserScore> {
        return database.userScoreDao().getLast();
    }

//    fun observeAllLeaderboard(): Flow<List<UserScore>> { // TODO: mozda popraviti
//        return database.leaderboardDao().observeAll()
//    }

    fun saveUserScore(userScore: UserScore) {
        return database.userScoreDao().insert(userScore)
    }

    suspend fun postUserScore(nickname: String, result: Double, category: Int): LeaderboardApiModelResponse {
        return leaderboardApi.postLeaderboard(LeaderboardApi.LeaderboardPostBody(nickname, result.toFloat(), category))
    }
}