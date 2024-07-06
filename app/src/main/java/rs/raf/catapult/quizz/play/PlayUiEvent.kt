package rs.raf.catapult.quizz.play

sealed class PlayUiEvent {
    data object OnPlayClick : PlayUiEvent()
    data object OnLeaderboardClick : PlayUiEvent()
    data object OnCategoriesClose: PlayUiEvent()
    data class OnCategoryClick(val categoryId: Int): PlayUiEvent()
}