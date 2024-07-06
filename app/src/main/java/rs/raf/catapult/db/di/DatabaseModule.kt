package rs.raf.catapult.db.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import rs.raf.catapult.db.AppDatabase
import rs.raf.catapult.db.AppDatabaseBuilder
import javax.inject.Singleton

@Module // govori hiltu kako da obezbedi instance nekih tipova ili kako da izvrsi DI za neke apstraktne klase
@InstallIn(SingletonComponent::class) // definise scope i zivotni ciklus modula, u ovom slucaju modul ce se instalirati
                                      // u singleton komponenti
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(builder : AppDatabaseBuilder) : AppDatabase {
        return builder.build() // ovako ne moramo da pisemo build nigde drugde
    }
}