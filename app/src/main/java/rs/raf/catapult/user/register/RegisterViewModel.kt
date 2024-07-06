package rs.raf.catapult.user.register

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import rs.raf.catapult.breeds.list.BreedListState
import rs.raf.catapult.user.datastore.UserStore
import rs.raf.catapult.user.datastore.model.UserModel
import javax.inject.Inject


@HiltViewModel
class RegisterViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
): ViewModel() {

    @Inject lateinit var userStore: UserStore

    private val events = MutableSharedFlow<RegisterUiEvent>()
    private val _state = MutableStateFlow(RegisterState())
    val state: StateFlow<RegisterState> = _state
    private fun setState(reducer: RegisterState.() -> RegisterState) = _state.update(reducer)

    fun setEvent(event: RegisterUiEvent) = viewModelScope.launch { events.emit(event) }

    init {
        observeEvents()
    }

    private fun observeEvents() {
        viewModelScope.launch {
            events.collect { event ->
                when (event) {
                    is RegisterUiEvent.OnRegisterClick -> {
                        val (email, nickname, firstName, lastName) = event

                        try {
                            val user = UserModel(email, nickname, firstName, lastName)

                            userStore.updateUser(user)

                        } catch (e: IllegalArgumentException) {
                            setState { copy(error = e.message ?: "Unknown error") }
                        }
                    }
                }
            }
        }
    }

}


