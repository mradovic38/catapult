package rs.raf.catapult.breeds.list

import rs.raf.catapult.breeds.list.model.BreedListElementUiModel

data class BreedListState(
    val initialLoading : Boolean = false,
    val fetching: Boolean = false,
    val breeds: List<BreedListElementUiModel> = emptyList(),
    val error: ListError? = null,
    val query: String = "",
    val isSearchMode: Boolean = false,

    ) {
    sealed class ListError {
        data class ListUpdateFailed(val cause: Throwable? = null) : ListError()
    }
}
