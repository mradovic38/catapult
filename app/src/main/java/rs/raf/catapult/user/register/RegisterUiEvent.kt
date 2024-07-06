package rs.raf.catapult.user.register

sealed class RegisterUiEvent {
    data class OnRegisterClick(val email: String, val nickname: String, val firstName: String, val lastName: String) : RegisterUiEvent()
}