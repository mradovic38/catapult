package rs.raf.catapult.user.datastore.model

import kotlinx.serialization.Serializable

@Serializable
data class UserModel(
    val email: String,
    val nickname: String,
    val firstName: String,
    val lastName: String,
    val isDefault: Boolean = false
) {
    fun isEmpty(): Boolean {
        return email.isBlank() && nickname.isBlank() && firstName.isBlank() && lastName.isBlank()
    }

    init {
        if (!isDefault) {
            require(
                email.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
            ) {
                "Invalid email address"
            }

            require(firstName.isNotEmpty()) {
                "First name must not be empty"
            }

            require(lastName.isNotEmpty()) {
                "Last name must not be empty"
            }

            require(nickname.matches(Regex("^[a-zA-Z0-9_]*$"))) {
                "Nickname should only contain letters, numbers and underscore"
            }
        }
    }
}