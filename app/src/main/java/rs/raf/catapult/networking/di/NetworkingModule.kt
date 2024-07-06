package rs.raf.catapult.networking.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import rs.raf.catapult.BuildConfig
import rs.raf.catapult.serialization.AppJson
import javax.inject.Named
import javax.inject.Singleton

@Module // govori hiltu kako da obezbedi instance nekih tipova ili kako da izvrsi DI za neke apstraktne klase
@InstallIn(SingletonComponent::class) // definise scope i zivotni ciklus modula, u ovom slucaju modul ce se instalirati
                                      // u singleton komponenti
object NetworkingModule {

    @Singleton
    @Provides
    @Named("CatsApi") // umesto ovoga moglo je i @Qualifier nad annotation klasom, pa @ImeAnotacije umesto ovoga,
    // onda kad injectujemo stavimo @ImeAnotacije private val ... u konstruktoru
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor {
                val updatedRequest = it.request().newBuilder()
                    .addHeader("x-api-key", BuildConfig.CATS_API_KEY)
                    .build()
                it.proceed(updatedRequest)
            }
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    setLevel(HttpLoggingInterceptor.Level.BODY)
                }
            )
            .build()
    }

    @Provides
    @Singleton
    @Named("LeaderboardApi")
    fun provideLeaderboardOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    setLevel(HttpLoggingInterceptor.Level.BODY)
                }
            )
            .build()
    }


    @Singleton
    @Provides
    @Named("CatsApi")
    fun provideRetrofitClient(@Named("CatsApi") okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.thecatapi.com/v1/")
            .client(okHttpClient)
            .addConverterFactory(AppJson.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    @Provides
    @Singleton
    @Named("LeaderboardApi")
    fun provideLeaderboardRetrofitClient(@Named("LeaderboardApi") okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://rma.finlab.rs/")
            .client(okHttpClient)
            .addConverterFactory(AppJson.asConverterFactory("application/json".toMediaType()))
            .build()
    }


}