package rs.raf.catapult.breeds.details

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import rs.raf.catapult.breeds.details.model.BreedDetailsUiModel
import rs.raf.catapult.breeds.details.model.BreedImageUiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import rs.raf.catapult.breeds.db.BreedWithPhotos
import rs.raf.catapult.breeds.db.BreedWithTemperaments
import rs.raf.catapult.breeds.details.model.BreedWithPhotosUiModel
import rs.raf.catapult.breeds.repository.BreedRepository
import rs.raf.catapult.breeds.db.Photo
import javax.inject.Inject

@HiltViewModel
class BreedDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: BreedRepository,
) : ViewModel() {

    private val breedId: String = savedStateHandle["id"] ?: throw IllegalArgumentException("breedId is required.")

    private val _state = MutableStateFlow(BreedDetailsState(breedId = breedId))
    val state = _state.asStateFlow()
    private fun setState(reducer: BreedDetailsState.() -> BreedDetailsState) =
        _state.getAndUpdate(reducer)


    private val events = MutableSharedFlow<DetailsUiEvent>()
    fun setEvent(event: DetailsUiEvent) {
        viewModelScope.launch {
            events.emit(event)
        }
    }

    init {
        observeEvents()
        fetchBreedDetails()
        observeBreedWithPhotos()
    }

    private fun observeEvents() { // osluskuje eventove (klik na dugme)
        viewModelScope.launch {
            events.collect {
                when (it) {
                    is DetailsUiEvent.RequestGoToWebsite -> {
                            goToWebsite(context = it.context, id = it.url)
                    }
                }
            }
        }
    }

    private fun observeBreedWithPhotos(){
        viewModelScope.launch {
            repository.observeBreed(breedId)
                .distinctUntilChanged()
                .collect {
                    setState { copy(breed = it.asBreedPhotoUiModel().breed,
                        images = it.asBreedPhotoUiModel().photos.shuffled().take(1)) }
                }
        }
    }



    private fun fetchBreedDetails() {
        viewModelScope.launch {
            setState { copy(fetching = true) } // state postaje fetching - ovo je vise kao updating sada
            try {
                withContext(Dispatchers.IO) {
                    repository.fetchBreedAndPhotos(breedId)
                }
            } catch (error: Exception) {
                //setState { copy(error = BreedDetailsState.DetailsError.DataUpdateFailed(cause = error)) }

            } finally {
                setState { copy(fetching = false) }
            }
        }
    }




    private suspend fun goToWebsite(context: Context, id: String) {
        withContext(Dispatchers.IO) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(id))
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }


    // konvertuje iz api u ui model
    private fun BreedWithTemperaments.asBreedUiModel() = BreedDetailsUiModel(
        id = this.breed.id,
        name = this.breed.name,
        altNames = this.breed.altNames,
        description = this.breed.description,
        temperament = this.temperaments.map{it.name},
        adaptability = this.breed.adaptability,
        affectionLevel = this.breed.affectionLevel,
        avgWeightMet = this.breed.avgWeightMet,
        avgWeightImp = this.breed.avgWeightImp,
        childFriendly = this.breed.childFriendly,
        dogFriendly = this.breed.dogFriendly,
        healthIssues = this.breed.healthIssues,
        lifeSpan = this.breed.lifeSpan,
        origin = this.breed.origin,
        rare = this.breed.rare,
        sheddingLevel = this.breed.sheddingLevel,
        socialNeeds = this.breed.socialNeeds,
        wikipediaUrl = this.breed.wikipediaUrl

    )

    // konvertuje iz api u ui model
    private fun BreedWithPhotos.asBreedPhotoUiModel() = BreedWithPhotosUiModel(
       breed = this.breed.asBreedUiModel(),
        photos = this.photos.map { it.asBreedImageUiModel() }

    )


    private fun Photo.asBreedImageUiModel() = BreedImageUiModel(
        id = this.id,
        url = this.url
    )
}
