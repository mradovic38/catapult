package rs.raf.catapult.breeds.api.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import rs.raf.catapult.breeds.api.BreedsApi
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BreedsApiModule {
    @Provides
    @Singleton
    fun provideBreedsApi(@Named("CatsApi") retrofit: Retrofit): BreedsApi = retrofit.create()

}