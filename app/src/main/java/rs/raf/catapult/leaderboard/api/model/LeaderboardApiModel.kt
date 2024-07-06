package rs.raf.catapult.leaderboard.api.model

import kotlinx.serialization.Serializable

@Serializable
data class LeaderboardApiModel (
//    "result": { "category": 1, "nickname": "rma", "result": 88.88, "createdAt": 1717624105670 },
//       "ranking": 1

    val category: Int,
    val nickname: String,
    val result: Float,
    val createdAt: Long
)

@Serializable
data class LeaderboardApiModelResponse (
    val result: LeaderboardApiModel,
    val ranking: Int
)
