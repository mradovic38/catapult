package rs.raf.catapult.breeds.details

import android.content.Context

sealed class DetailsUiEvent {

    data class RequestGoToWebsite(val context: Context, val url: String) : DetailsUiEvent()
}