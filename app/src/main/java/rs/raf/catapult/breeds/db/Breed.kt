package rs.raf.catapult.breeds.db

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation
import androidx.room.TypeConverter
import androidx.room.TypeConverters


@Entity
@TypeConverters(BreedTypeConverters::class)
data class Breed (
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val altNames: String,

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
)


