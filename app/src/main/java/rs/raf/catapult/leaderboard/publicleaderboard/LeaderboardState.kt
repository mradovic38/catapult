package rs.raf.catapult.leaderboard.publicleaderboard

import rs.raf.catapult.leaderboard.publicleaderboard.model.LeaderboardElementUiModel

data class LeaderboardState (
    val leaderboard: List<LeaderboardElementUiModel> = emptyList(),
    val error: ListError? = null,
    val fetching: Boolean = false
){

    sealed class ListError {
        data class ListUpdateFailed(val cause: Throwable? = null) : ListError()
    }
}