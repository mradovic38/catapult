package rs.raf.catapult.breeds.list

sealed class ListUiEvent {
    data class SearchQueryChanged(val query: String) : ListUiEvent()
    data class SearchClicked(val query: String) : ListUiEvent()
    data object CloseSearchMode : ListUiEvent()
    data class ChangeSearchBarActive(val status: Boolean) : ListUiEvent()
}