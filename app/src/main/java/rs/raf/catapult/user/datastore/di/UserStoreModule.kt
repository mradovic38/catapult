package rs.raf.catapult.user.datastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import rs.raf.catapult.user.datastore.serialization.UserDataSerializer
import rs.raf.catapult.user.datastore.model.UserModel
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserStoreModule {

    @Provides
    @Singleton
    fun provideUserDataStore(
        @ApplicationContext context: Context
    ): DataStore<UserModel> =
        DataStoreFactory.create(
            produceFile = { context.dataStoreFile("user-data.json") },
            serializer = UserDataSerializer()
        )
}