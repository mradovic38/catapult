package rs.raf.catapult.leaderboard.publicleaderboard.model

import java.util.Date

data class LeaderboardElementUiModel (
    val id: Int,
    val nickname: String,
    val createdAt: Date,
    val score: Double,
    val totalQuizzes: Int
    )