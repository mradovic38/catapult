package rs.raf.catapult.quizz.questions

data class QuestionsState (
    val initCountdownValue: Int = 4,
    val quizzCountownValue: Int = 300,
    val questions: List<Question> = emptyList(),
    val error: String = "",
    val currentQuestionIndex: Int = 0,
    val correctAnswers: Int = 0,
    val score: Double = -1.0,
    val showBackDialog: Boolean = false,

)