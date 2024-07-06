package rs.raf.catapult.quizz.questions

sealed class QuestionsUiEvent {
    data object OnQuizzStart : QuestionsUiEvent()
    data class OnQuizzEnd(val pvt: Int, val bto :Int) : QuestionsUiEvent()
    data object OnNextQuestion : QuestionsUiEvent()
    data object OnCorrectAnswer : QuestionsUiEvent()
    data object OnShareClick: QuestionsUiEvent()
    data object OnBackClick: QuestionsUiEvent()
    data object OnBackDismiss: QuestionsUiEvent()
}