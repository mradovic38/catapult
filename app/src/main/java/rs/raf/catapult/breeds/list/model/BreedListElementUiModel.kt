package rs.raf.catapult.breeds.list.model

data class BreedListElementUiModel (
    val id: String,
    val name: String,
    val description: String,
    val altNames: String,
    val temperament: List<String>,
)