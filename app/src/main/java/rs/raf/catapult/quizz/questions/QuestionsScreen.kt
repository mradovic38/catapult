package rs.raf.catapult.quizz.questions

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import rs.raf.catapult.core.compose.Countdown
import rs.raf.catapult.core.compose.GuessTheBreedComposable
import rs.raf.catapult.core.compose.QuizzResults
import rs.raf.catapult.core.theme.CatapultTheme

fun NavGraphBuilder.questions(
    route: String,
    onScoreShow: (Double) -> Unit,
    onBack: ()-> Unit
) = composable(
    route = route,
    popEnterTransition =  {expandIn(
        animationSpec = tween(1000)
    ) },
    popExitTransition = {
        shrinkOut (
        animationSpec = tween(1000)
    )
    },
) { navBackStackEntry ->
    val questionsViewModel = hiltViewModel<QuestionsViewModel>(navBackStackEntry)

    BackHandler {
        questionsViewModel.setEvent(QuestionsUiEvent.OnBackClick)
    }
    val state = questionsViewModel.state.collectAsState().value
    QuestionsScreen(
        state,
        onScoreShow = onScoreShow,
        onBack = onBack,
        onBackDismiss = { questionsViewModel.setEvent(QuestionsUiEvent.OnBackDismiss) },
        onCorrectAnswer = { questionsViewModel.setEvent(QuestionsUiEvent.OnCorrectAnswer) },
        onNextQuestion = { questionsViewModel.setEvent(QuestionsUiEvent.OnNextQuestion) },
        onQuizzEnd = { countdown, correctAnswers -> questionsViewModel.setEvent(QuestionsUiEvent.OnQuizzEnd(countdown, correctAnswers)) }
    )



}


@Composable
fun QuestionsScreen(
    state: QuestionsState,
    onScoreShow: (Double) -> Unit,
    onBack: () -> Unit,
    onBackDismiss:() -> Unit,
    onCorrectAnswer: () -> Unit,
    onNextQuestion:() -> Unit,
    onQuizzEnd: (Int, Int) -> Unit,
) {

    if (state.showBackDialog) {
        AlertDialog(
            onDismissRequest = { onBackDismiss() },
            title = { Text("Are you sure?") },
            text = { Text("Are you sure that you want to exit?\nAll progress will be deleted!") },
            confirmButton = {
                TextButton(onClick = {
                    onBackDismiss()
                    onBack() }) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(onClick = { onBackDismiss() }) {
                    Text("No")
                }
            }
        )
    }

    Scaffold { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    AnimatedVisibility(
                        visible = state.initCountdownValue in 1..3,
                        enter = expandIn(
                            expandFrom = Alignment.Center,
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessLow
                            )
                        ),
                        exit = shrinkOut(
                            shrinkTowards = Alignment.Center,
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessLow
                            )
                        )
                    ) {
                        Text(
                            text = "Get ready!",
                            style = MaterialTheme.typography.displayLarge,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(16.dp)
                                .alpha(0.8f)
                        )
                    }
                    Spacer(modifier = Modifier.size(16.dp))
                    AnimatedVisibility(
                        visible = state.initCountdownValue in 1..3,
                        enter = expandIn(
                            expandFrom = Alignment.Center,
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessLow
                            )
                        ),
                        exit = shrinkOut(
                            shrinkTowards = Alignment.Center,
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessLow
                            )
                        )
                    ) {
                        Countdown(
                            modifier = Modifier.padding(paddingValues),
                            canvasModifier = Modifier.size(152.dp),
                            textStyle = MaterialTheme.typography.displayLarge,
                            number = state.initCountdownValue,
                            max = 3,
                        )
                    }

                }

        }
        if (state.questions.isNotEmpty() && state.initCountdownValue < 0 && state.quizzCountownValue >= 0 && state.currentQuestionIndex < state.questions.size) {
            AnimatedVisibility(
                visible = true,
                enter = expandIn(),
                exit = shrinkOut()
            ) {
                Crossfade(targetState = state.currentQuestionIndex, label = "crossfade") {

                    val (textColor, boxColor) = when (state.questions[it].type) {
                        1 -> Pair(MaterialTheme.colorScheme.onPrimary, MaterialTheme.colorScheme.primary)
                        2 -> Pair(MaterialTheme.colorScheme.onSecondary, MaterialTheme.colorScheme.secondary)
                        else -> Pair(MaterialTheme.colorScheme.onTertiary, MaterialTheme.colorScheme.tertiary)
                    }
                    GuessTheBreedComposable(
                        questionIdx = it + 1,
                        text = state.questions[it].text,
                        url = state.questions[it].photoUrl,
                        options = state.questions[it].options,
                        textColor = textColor,
                        textBoxColor = boxColor,

                        onOptionClick = { selectedOption ->
                            // tacno
                            if (selectedOption == state.questions[it].correctOption) {
                                onCorrectAnswer()
                            }
                            // netacno
                            else {

                            }

                            // sledece pitanje
                            onNextQuestion()
                        }
                    )
                }
            }
        }
        else if (state.initCountdownValue < 0 && state.questions.isEmpty()){
            CircularProgressIndicator(
                modifier = Modifier.size(36.dp),
            )
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.TopStart
        ) {
            AnimatedVisibility(
                visible = state.questions.isNotEmpty() && state.initCountdownValue < 0 && state.quizzCountownValue >= 0 && state.currentQuestionIndex < state.questions.size,
                enter = expandIn(
                    expandFrom = Alignment.Center,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                ),
                exit = shrinkOut(
                    shrinkTowards = Alignment.Center,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
            ) {
                    Countdown(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(8.dp),
                        canvasModifier = Modifier.size(72.dp),
                        textStyle = MaterialTheme.typography.headlineSmall,
                        number = state.quizzCountownValue,
                        max = 300,
                    )

            }

        }
        if ((state.quizzCountownValue < 0 || state.currentQuestionIndex >= state.questions.size) && state.initCountdownValue < 0 && state.score<0)
            onQuizzEnd(state.quizzCountownValue, state.correctAnswers)

        }

        if(state.score >=0){
           onScoreShow(state.score)
        }

}




@Preview
@Composable
fun QuestionsScreenPreview(){
    CatapultTheme {
        QuestionsScreen(
            state = QuestionsState(
                questions = listOf(
                    Question(
                        text = "What is the breed of this dog?",
                        photoUrl = "https://images.dog.ceo/breeds/terrier-irish/n02093991_1003.jpg",
                        options = listOf("terrier", "bulldog", "poodle", "labrador"),
                        correctOption = "terrier",
                        type = 1
                    ),
                ),
                initCountdownValue = -1,
                quizzCountownValue = 60
            ),
            onBackDismiss = {},
            onBack = {},
            onCorrectAnswer = {},
            onNextQuestion = {},
            onQuizzEnd = { _, _ -> },
            onScoreShow = {}
        )
    }

}


