package rs.raf.catapult.db

import android.content.Context
import androidx.room.Room
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AppDatabaseBuilder @Inject constructor(
    @ApplicationContext private val context: Context // obezbedjuje context tako da ne moramo iz application klase
) {
    fun build() : AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "catapult-db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
}