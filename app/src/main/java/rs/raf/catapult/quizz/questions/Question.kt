package rs.raf.catapult.quizz.questions

data class Question(
    val text: String,
    val type: Int,
    val photoUrl: String,
    val options: List<String>,
    val correctOption: String
)