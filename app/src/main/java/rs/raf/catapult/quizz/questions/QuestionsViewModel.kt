package rs.raf.catapult.quizz.questions

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import dagger.hilt.android.internal.Contexts.getApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import rs.raf.catapult.breeds.repository.BreedRepository
import rs.raf.catapult.leaderboard.db.UserScore
import rs.raf.catapult.leaderboard.repository.LeaderboardRepository
import rs.raf.catapult.breeds.db.Photo
import rs.raf.catapult.user.datastore.UserStore
import javax.inject.Inject

@HiltViewModel
class QuestionsViewModel @Inject constructor(
    private val application: Application,
    private val breedRepository: BreedRepository,
    private val leaderboardRepository: LeaderboardRepository,
    private val userStore: UserStore

): ViewModel() {

    private val events = MutableSharedFlow<QuestionsUiEvent>()
    private val _state = MutableStateFlow(QuestionsState())
    val state: StateFlow<QuestionsState> = _state

    private fun setState(reducer: QuestionsState.() -> QuestionsState) = _state.update(reducer)
    fun setEvent(event: QuestionsUiEvent) = viewModelScope.launch { events.emit(event) }

    init {
        observeEvents()
        generateQuestions()
        startInitialCountdown()
    }

    private fun generateQuestions() = viewModelScope.launch {
        try {
            val createdQuestions = mutableListOf<Question>()

            // da bi se izbeglo ponavljanje slika
            val blacklistPhotos = mutableListOf<String>()

            withContext(Dispatchers.IO) {
                for (i in 1..20) {
                    val res: Pair<Question, String> = when (kotlin.random.Random.nextInt(3)) {
                        0 -> generateGuessTheBreedQuestion(blacklistPhotos)
                        1 -> generateGuessTheTemperamentQuestion(blacklistPhotos)
                        else -> generateEliminateTheTemperamentQuestion(blacklistPhotos)
                    }

                    blacklistPhotos.add(res.second)
                    createdQuestions.add(res.first)
                    Log.d("QUESTIONS", "Question $i: ${res.first}")
                }
                prefetchQuestionImages(createdQuestions)
            }


            setState { copy(questions = createdQuestions) }
        }
        catch (e: Exception) {
            setState { copy(error = e.message ?: "An error occurred")}
        }

    }


    private fun generateGuessTheBreedQuestion(blacklistPhotos: List<String>): Pair<Question, String> {
        // da bi random opcije bile razlicite
        val blacklistBreeds = mutableListOf<String>()
        val incorrectBreeds = mutableListOf<String>()

        // tacna rasa
        var correctBreed = breedRepository.getRandomBreed(emptyList())
        var photo: Photo? = null

        // resava slucaj ako su sve slike za tu rasu vec iskoriscene
        while (photo == null) {
            correctBreed = breedRepository.getRandomBreed(emptyList())
            photo = try {
                breedRepository.getRandomPhotoOfBreed(correctBreed.breed.id, blacklistPhotos)
            } catch (e: Exception) {
                null
            }
        }
        blacklistBreeds.add(correctBreed.breed.id)

        // generisanje 3 netacne opcije
        for(i in 1..3){
            val toAdd = breedRepository.getRandomBreed(blacklistBreeds)

            incorrectBreeds.add(toAdd.breed.name)
            blacklistBreeds.add(toAdd.breed.id)
        }

        return Pair(Question(
            "Guess the Cat Breed",
            1,
            photo.url,
            (incorrectBreeds + correctBreed.breed.name).shuffled(),
            correctBreed.breed.name
        ), photo.id)
    }


    private suspend fun prefetchQuestionImages(questions: List<Question>) {
        viewModelScope.launch {
            val imageLoader = ImageLoader(application.applicationContext)
            questions.forEach { question ->
                val request = ImageRequest.Builder(application.applicationContext)
                    .data(question.photoUrl)
                    .build()
                val result = imageLoader.execute(request)
                if (result is SuccessResult) {
                    // slika kesirana
                }
            }
        }
    }

    private fun generateGuessTheTemperamentQuestion(blacklistPhotos: List<String>): Pair<Question, String> {
        // da bi random opcije bile razlicite
        val blackListTemperaments = mutableListOf<String>()
        val incorrectTemperaments = mutableListOf<String>()

        var correctBreed = breedRepository.getRandomBreed(emptyList())

        var photo: Photo? = null

        // resava slucaj ako su sve slike za tu rasu vec iskoriscene
        while (photo == null) {
            correctBreed = breedRepository.getRandomBreed(emptyList())
            photo = try {
                breedRepository.getRandomPhotoOfBreed(correctBreed.breed.id, blacklistPhotos)
            } catch (e: Exception) {
                null
            }
        }
        // tacan temperament
        val correctTemperament = correctBreed.temperaments.random().name
        blackListTemperaments.add(correctTemperament)

        // generisanje 3 netacne opcije
        for(i in 1..3){
            val toAdd = breedRepository.getRandomTemperament(blackListTemperaments).name

            incorrectTemperaments.add(toAdd)
            blackListTemperaments.add(toAdd)
        }


        return Pair(Question(
            "Match the Temperament",
            2,
            photo.url,
            (incorrectTemperaments + correctTemperament).shuffled(),
            correctTemperament
        ), photo.id)
    }

    private fun generateEliminateTheTemperamentQuestion(blacklistPhotos: List<String>): Pair<Question, String> {
        // da bi random opcije bile razlicite
        val blackListTemperaments = mutableListOf<String>()

        var correctBreed = breedRepository.getRandomBreed(emptyList())

        var photo: Photo? = null

        // resava slucaj ako su sve slike za tu rasu vec iskoriscene
        while (photo == null || correctBreed.temperaments.size < 3) {
            correctBreed = breedRepository.getRandomBreed(emptyList())
            photo = try {
                breedRepository.getRandomPhotoOfBreed(correctBreed.breed.id, blacklistPhotos)
            } catch (e: Exception) {
                null
            }
        }

        // temperamenti koji su tacni
        var correctTemperaments = correctBreed.temperaments.map{ it.name}
        blackListTemperaments.addAll(correctTemperaments)
        correctTemperaments = correctTemperaments.shuffled().take(3)

        // generisanje 1 uljeza temperamenta
        val correctAnswer = breedRepository.getRandomTemperament(blackListTemperaments).name



        return Pair(Question(
            "Find the Odd Temperament Out",
            3,
            photo.url,
            (correctTemperaments + correctAnswer).shuffled(),
            correctAnswer
        ), photo.id)
    }



    private fun startInitialCountdown() = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            for (i in 3 downTo -1) {
                delay(1000L)
                setState {  copy(initCountdownValue = i)}
            }
        }

        startQuizCountdown()
    }


    private fun startQuizCountdown() = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            for (i in 299 downTo -1) {
                delay(1000L)
                setState { copy(quizzCountownValue = i)}
            }
        }
    }

    private fun observeEvents() {
        viewModelScope.launch {
            events.collect { event ->
                when (event) {
                    QuestionsUiEvent.OnQuizzStart -> {
                    }


                    QuestionsUiEvent.OnNextQuestion -> {
                        setState { copy(currentQuestionIndex = currentQuestionIndex + 1) }
                        Log.d("QUESTIONS", "NEXT QUESTION")
                    }

                    QuestionsUiEvent.OnCorrectAnswer -> {
                        setState { copy(correctAnswers = correctAnswers + 1) }
                    }

                    is QuestionsUiEvent.OnQuizzEnd -> {
                        val mvt = 300.0
//                        val ubp = event.bto * 2.5 * (1 + (event.pvt + 120) / mvt)

                        setState {
                            copy(
                                score = (event.bto * 2.5 * (1 + (event.pvt + 120) / mvt))
                                    .coerceAtMost(maximumValue = 100.0)
                            )
                        }

                        withContext(Dispatchers.IO) {
                             leaderboardRepository.saveUserScore(UserScore(name = userStore.userData.value.nickname,
                                 score = _state.value.score,
                                 isPublic = false))
                        }

                    }

                    QuestionsUiEvent.OnShareClick -> {


                    }

                    QuestionsUiEvent.OnBackClick -> {
                        setState { copy(showBackDialog = true)}
                    }
                    QuestionsUiEvent.OnBackDismiss -> {
                        setState { copy(showBackDialog = false)}
                    }
                }
            }
        }
    }

}