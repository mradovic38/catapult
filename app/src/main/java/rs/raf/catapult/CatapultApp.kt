package rs.raf.catapult

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.HttpException
import rs.raf.catapult.breeds.repository.BreedRepository
import rs.raf.catapult.user.datastore.UserStore
import javax.inject.Inject

@HiltAndroidApp // ovo okida generisanje koda
class CatapultApp : Application() {

//    private val scope = CoroutineScope(Dispatchers.IO)

    @Inject
    lateinit var userStore: UserStore


    override fun onCreate() {
        super.onCreate()

        val authData = userStore.userData.value
        Log.d("DATASTORE", "Auth data: $authData")

    }


}