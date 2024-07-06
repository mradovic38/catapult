package rs.raf.catapult.breeds.api.model

import kotlinx.serialization.Serializable


    @Serializable
    data class BreedImageApiModel(
        val id: String,
        val url: String,
        val width: Int,
        val height: Int,

    )


    /*
    [{
    "id": "ozEvzdVM-",
    "url": "https://cdn2.thecatapi.com/images/ozEvzdVM-.jpg",
    "width": 1200,
    "height": 800
}]
     */
