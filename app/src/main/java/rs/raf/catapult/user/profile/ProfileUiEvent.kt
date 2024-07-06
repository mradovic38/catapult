package rs.raf.catapult.user.profile

sealed class ProfileUiEvent {
    data object OnEditClick : ProfileUiEvent()
    data class OnConfirmClick(val email: String, val nickname: String, val firstName: String, val lastName: String) : ProfileUiEvent()
}