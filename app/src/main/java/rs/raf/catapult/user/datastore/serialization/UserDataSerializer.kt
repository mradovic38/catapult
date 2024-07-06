package rs.raf.catapult.user.datastore.serialization

import androidx.datastore.core.Serializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import rs.raf.catapult.serialization.AppJson
import rs.raf.catapult.user.datastore.model.UserModel
import java.io.InputStream
import java.io.OutputStream

class UserDataSerializer: Serializer<UserModel> {
    override val defaultValue: UserModel = UserModel(
        email = "",
        nickname = "",
        firstName = "",
        lastName = "",
        isDefault = true
    )

    override suspend fun readFrom(input: InputStream): UserModel {
        return withContext(Dispatchers.IO) {
            val jsonString = input.bufferedReader().use { it.readText() }
            AppJson.decodeFromString(UserModel.serializer(), jsonString)
        }
    }

    override suspend fun writeTo(t: UserModel, output: OutputStream) {
        withContext(Dispatchers.IO) {
            val jsonString = AppJson.encodeToString(UserModel.serializer(), t)
            output.write(jsonString.toByteArray())
        }
    }

}