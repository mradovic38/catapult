package rs.raf.catapult.quizz.results

data class ResultsState (
    val score: Double = 0.0,
    val nickname: String = "",
    val error: String = "",
    val scoreId: Int = 0
)