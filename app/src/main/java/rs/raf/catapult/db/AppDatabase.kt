package rs.raf.catapult.db

import androidx.room.Database
import androidx.room.RoomDatabase
import rs.raf.catapult.breeds.db.Breed
import rs.raf.catapult.breeds.db.BreedDao
import rs.raf.catapult.breeds.db.BreedTemperament
import rs.raf.catapult.breeds.db.Temperament
import rs.raf.catapult.leaderboard.db.UserScore
import rs.raf.catapult.leaderboard.db.UserScoreDao
import rs.raf.catapult.breeds.db.Photo
import rs.raf.catapult.breeds.db.PhotoDao

@Database(
    entities = [
        Breed::class,
        Temperament::class,
        BreedTemperament::class,
        Photo::class,
        UserScore::class
                     ],
    version = 4,
    exportSchema = true // obezbedjuje sliku baze unutar file sistema kako bi kasnije to moglo da se iskoristi
                        // da se ne bi stalno popunjavala od nule
)

abstract class AppDatabase : RoomDatabase(){
    abstract fun breedDao(): BreedDao
    abstract fun photoDao(): PhotoDao
    abstract fun userScoreDao(): UserScoreDao

}