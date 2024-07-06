package rs.raf.catapult.breeds.details.model

data class BreedDetailsUiModel(

    val id: String,
    val name: String,
    val description: String,
    val altNames: String,
    val temperament: List<String>,
    val origin: String,
    val wikipediaUrl: String="",
    val lifeSpan: String,
    val adaptability: Int,
    val affectionLevel: Int,
    val childFriendly: Int,
    val dogFriendly: Int,
    val healthIssues: Int,
    val sheddingLevel: Int,
    val socialNeeds: Int,
    val rare: Int,
    val avgWeightImp: Double,
    val avgWeightMet: Double,

    ) {
//    fun isValid(): Boolean {
//        return Patterns.EMAIL_ADDRESS.matcher(email).matches() && password.isNotEmpty() && website.isNotEmpty()
//    }
}