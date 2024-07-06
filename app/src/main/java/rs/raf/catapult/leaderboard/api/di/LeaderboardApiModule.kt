package rs.raf.catapult.leaderboard.api.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import rs.raf.catapult.leaderboard.api.LeaderboardApi
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LeaderboardApiModule {
    @Provides
    @Singleton
    fun provideLeaderboardApi(@Named("LeaderboardApi") retrofit: Retrofit, ): LeaderboardApi = retrofit.create()

}