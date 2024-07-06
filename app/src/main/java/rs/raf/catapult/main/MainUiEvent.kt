package rs.raf.catapult.main

sealed class MainUiEvent {
    data object RetryClicked: MainUiEvent()
}