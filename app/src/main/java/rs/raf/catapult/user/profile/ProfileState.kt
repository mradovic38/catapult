package rs.raf.catapult.user.profile

import rs.raf.catapult.user.profile.model.ResultUiModel

data class ProfileState (
    val fetching: Boolean = false,
    val email: String = "",
    val nickname: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val error: String = "",
    val editMode: Boolean = false,
    val bestRanking: Int = 0,
    val bestScore: Float = 0f,
    val allResults: List<ResultUiModel> = emptyList(),
    val resutsError: String = ""
)