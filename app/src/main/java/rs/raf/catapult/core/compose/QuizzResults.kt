package rs.raf.catapult.core.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import rs.raf.catapult.core.theme.CatapultTheme

@Composable
fun QuizzResults(
    score: Double,
    onShareClick: () -> Unit,
    error: String = "",
    onBackClick: () -> Unit
){

    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { paddingValues ->





        var isScreenVisible by remember { mutableStateOf(false) }
        var isLeaderboardsButtonVisible by remember { mutableStateOf(false) }
        var isScoreVisiblee by remember { mutableStateOf(false) }
        var isLeaderboardsDescriptionVisible by remember { mutableStateOf(false) }
        var isScoreDescriptionVisible by remember { mutableStateOf(false) }
        var isGameOverVisible by remember { mutableStateOf(false) }


        AnimatedVisibility( visible = isScoreDescriptionVisible,
            enter = slideInHorizontally(initialOffsetX = { it }), // Slide in from bottom
            exit = slideOutHorizontally(targetOffsetX = { it }) ) {

            IconButton(
                onClick = onBackClick,
                modifier = Modifier.padding(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBackIosNew,
                    contentDescription = "Back"
                )
            }
        }

        AnimatedVisibility(
            visible = isGameOverVisible,
            enter = slideInVertically(initialOffsetY = { -it }),
            exit = slideOutVertically(targetOffsetY = { -it })
        ) {
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .clip(MaterialTheme.shapes.large)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    "Game Over!",
                    style = MaterialTheme.typography.displayMedium,
                    textAlign = TextAlign.Center
                )
            }
        }
        LaunchedEffect(Unit) {
            isGameOverVisible = true
            delay(1250)
            isGameOverVisible = false
            delay(250)
            isScreenVisible = true
            delay(500)
            isScoreDescriptionVisible = true
            delay(500)
            isScoreVisiblee = true
            delay(500)
            isLeaderboardsButtonVisible = true
            delay(500)
            isLeaderboardsDescriptionVisible = true
            delay(500)

        }
        AnimatedVisibility(
            visible = isScreenVisible,
            enter = slideInVertically(initialOffsetY = { it }), // Slide in from bottom
            exit = slideOutVertically(targetOffsetY = { it }) // Slide out to bottom
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(.9f)
                        .fillMaxHeight(.5f)
                        .clip(MaterialTheme.shapes.large)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {


                    Text(
                        text = "Congratulations!",
                        style = MaterialTheme.typography.displaySmall,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 32.dp),

                        color = MaterialTheme.colorScheme.primary
                    )

                    AnimatedVisibility(
                        visible = isScoreVisiblee,
                        enter = expandIn(
                            expandFrom = Alignment.BottomCenter,
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessLow
                            )
                        )
                    ) {

                        Text(
                            text = "Score:",
                            style = MaterialTheme.typography.headlineMedium,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }

                    AnimatedVisibility(
                        visible = isScoreVisiblee,
                        enter = expandIn(
                            expandFrom = Alignment.Center,
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessLow
                            )
                        )
                    ) {


                        Box(
                            modifier = Modifier
                                .clip(MaterialTheme.shapes.extraLarge)
                                .border(
                                    2.dp,
                                    MaterialTheme.colorScheme.onPrimaryContainer,
                                    MaterialTheme.shapes.extraLarge
                                )
                                .background(MaterialTheme.colorScheme.primaryContainer)
                                .padding(horizontal = 16.dp)
                        ) {
                            Text(
                                text = "%.2f".format(score) + "%",
                                style = MaterialTheme.typography.displayLarge,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.primary,
                            )
                        }
                    }
                    Spacer(modifier = Modifier.size(16.dp))

                    AnimatedVisibility(
                        visible = isLeaderboardsButtonVisible,
                        enter = expandIn(
                            expandFrom = Alignment.Center,
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessLow
                            )
                        )
                    ) {
                        FilledIconButton(
                            onClick = {
                                onShareClick()
                            },
                            modifier = Modifier
                                .size(64.dp),
                            shape = MaterialTheme.shapes.extraLarge,
                        ) {

                            Icon(
                                modifier = Modifier.fillMaxWidth(),
                                imageVector = Icons.Default.Share,
                                contentDescription = "Leaderboards"
                            )

                        }
                    }
                    Spacer(modifier = Modifier.size(2.dp))

                    AnimatedVisibility(
                        visible = isLeaderboardsDescriptionVisible,
                        enter = expandIn(
                            expandFrom = Alignment.BottomCenter,
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessLow
                            )
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .alpha(0.7f)
                                .padding(horizontal = 8.dp, vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(

                                text = "(",
                                style = MaterialTheme.typography.labelMedium.copy(color = MaterialTheme.colorScheme.onBackground)
                            )
                            Icon(
                                modifier = Modifier.size(16.dp),
                                imageVector = Icons.Default.EmojiEvents,
                                contentDescription = "Leaderboard",
                            )
                            Text(

                                text = "Share Your Score on a Public Leaderboard)",
                                style = MaterialTheme.typography.labelMedium.copy(color = MaterialTheme.colorScheme.onBackground)
                            )

                        }

                    }
                    AnimatedVisibility(
                        visible = isLeaderboardsDescriptionVisible && error.isNotEmpty(),
                        enter = expandIn(
                            expandFrom = Alignment.BottomCenter,
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessLow
                            )
                        )
                    ) {
                        Text(
                            text = error,
                            style = MaterialTheme.typography.labelMedium.copy(color = MaterialTheme.colorScheme.error)
                        )
                    }
                }
            }
        }
        }



}

@Preview
@Composable
fun QuizzResultsPreview(){
    CatapultTheme {
        QuizzResults(
            score = 12.34343,
            onShareClick = {},
            error = "error",
            onBackClick = {}
        )
    }

}