package rs.raf.catapult.quizz.results

sealed class ResultsUiEvent {
    data class OnShareClick(val doRedirect: () -> Unit) : ResultsUiEvent()
}