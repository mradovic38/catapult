package rs.raf.catapult.main

data class MainScreenState (
    val fetching: Boolean = false,
    val error: MainError? = null,

    ) {
        sealed class MainError {
            data class MainUpdateFailed(val cause: Throwable? = null) : MainError()
        }
    }
