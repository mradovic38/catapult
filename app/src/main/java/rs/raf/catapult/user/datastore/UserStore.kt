package rs.raf.catapult.user.datastore

import androidx.datastore.core.DataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.runBlocking
import rs.raf.catapult.user.datastore.model.UserModel
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class UserStore @Inject constructor(
    private val persistence: DataStore<UserModel>
) {
    private val scope = CoroutineScope(Dispatchers.IO) // ovo je citanje i pisanje u data store-u

    val userData = persistence.data.stateIn(
        scope = scope,
        started = SharingStarted.Eagerly, // kad pocinje
        initialValue = runBlocking { persistence.data.first() } // prvi put kad se pokrene vraca prvi podatak
    )

    suspend fun updateUser(user: UserModel) {
        persistence.updateData { oldData ->
            oldData.copy(
                email = user.email,
                nickname = user.nickname.replace("\\s".toRegex(), ""),
                firstName = user.firstName,
                lastName = user.lastName
            )
        }
    }
}