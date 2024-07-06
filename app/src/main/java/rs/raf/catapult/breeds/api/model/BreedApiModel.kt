package rs.raf.catapult.breeds.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BreedsApiModel(
    val id: String,
    val name: String,
    val description: String,
    @SerialName("alt_names")
    val altNames: String="",
    val temperament: String,

    val origin: String,
    @SerialName("wikipedia_url")
    val wikipediaUrl: String="",

    @SerialName("life_span")
    val lifeSpan: String,
    val weight: Weight,

    val adaptability: Int,
    @SerialName("affection_level")
    val affectionLevel: Int,
    @SerialName("child_friendly")
    val childFriendly: Int,
    @SerialName("dog_friendly")
    val dogFriendly: Int,
    @SerialName("health_issues")
    val healthIssues: Int,
    @SerialName("shedding_level")
    val sheddingLevel: Int,
    @SerialName("social_needs")
    val socialNeeds: Int,

    val rare: Int,

    )

@Serializable
data class Weight(
    val imperial: String,
    val metric: String
)




// JSON:
//{
//    "weight": {
//    "imperial": "7  -  10",
//    "metric": "3 - 5"
//},
//    "id": "abys",
//    "name": "Abyssinian",
//    "cfa_url": "http://cfa.org/Breeds/BreedsAB/Abyssinian.aspx",
//    "vetstreet_url": "http://www.vetstreet.com/cats/abyssinian",
//    "vcahospitals_url": "https://vcahospitals.com/know-your-pet/cat-breeds/abyssinian",
//    "temperament": "Active, Energetic, Independent, Intelligent, Gentle",
//    "origin": "Egypt",
//    "country_codes": "EG",
//    "country_code": "EG",
//    "description": "The Abyssinian is easy to care for, and a joy to have in your home. They’re affectionate cats and love both people and other animals.",
//    "life_span": "14 - 15",
//    "indoor": 0,
//    "lap": 1,
//    "alt_names": "",
//    "adaptability": 5,
//    "affection_level": 5,
//    "child_friendly": 3,
//    "dog_friendly": 4,
//    "energy_level": 5,
//    "grooming": 1,
//    "health_issues": 2,
//    "intelligence": 5,
//    "shedding_level": 2,
//    "social_needs": 5,
//    "stranger_friendly": 5,
//    "vocalisation": 1,
//    "experimental": 0,
//    "hairless": 0,
//    "natural": 1,
//    "rare": 0,
//    "rex": 0,
//    "suppressed_tail": 0,
//    "short_legs": 0,
//    "wikipedia_url": "https://en.wikipedia.org/wiki/Abyssinian_(cat)",
//    "hypoallergenic": 0,
//    "reference_image_id": "0XYvRd7oD",
//    "image": {
//    "id": "0XYvRd7oD",
//    "width": 1204,
//    "height": 1445,
//    "url": "https://cdn2.thecatapi.com/images/0XYvRd7oD.jpg"
//}
//}







// Json:
//{
//    "id": 1,
//    "name": "Leanne Graham",
//    "username": "Bret",
//    "email": "Sincere@april.biz",
//    "address":
//    {
//        "street": "Kulas Light",
//        "suite": "Apt. 556",
//        "city": "Gwenborough",
//        "zipcode": "92998-3874",
//        "geo": {
//        "lat": "-37.3159",
//        "lng": "81.1496"
//    }
//    },
//    "phone": "1-770-736-8031 x56442",
//    "website": "hildegard.org",
//    "company":
//    {
//        "name": "Romaguera-Crona",
//        "catchPhrase": "Multi-layered client-server neural-net",
//        "bs": "harness real-time e-markets"
//    }
//}


