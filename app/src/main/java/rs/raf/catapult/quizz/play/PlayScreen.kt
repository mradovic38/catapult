package rs.raf.catapult.quizz.play

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.coroutines.delay
import rs.raf.catapult.R
import rs.raf.catapult.core.compose.ErrorDialog
import rs.raf.catapult.core.compose.LoadingDialog


fun NavGraphBuilder.play(
    route: String,
    onCategoryClick: (Int) -> Unit,
    onLeaderboardsButtonClicked: () -> Unit,
    onBack: () -> Unit
) = composable(
    route = route
    ) {navBackStackEntry ->
    val playViewModel = hiltViewModel<PlayViewModel>(navBackStackEntry)

    BackHandler {
        onBack()
    }
    val state by playViewModel.state.collectAsState()

    PlayScreen(
        state,
        onCategoryPicked = onCategoryClick,
        onLeaderboardsButtonClicked=onLeaderboardsButtonClicked,
        onPlayButtonClicked = {playViewModel.setEvent(PlayUiEvent.OnPlayClick) },
        onCategoriesClosed = {playViewModel.setEvent(PlayUiEvent.OnCategoriesClose) })

}

@Composable
fun PlayScreen(
    state: PlayState,
    onCategoryPicked: (Int) -> Unit,
    onCategoriesClosed: () -> Unit,
    onLeaderboardsButtonClicked: () -> Unit,
    onPlayButtonClicked: () -> Unit,) {



    var isPlayButtonVisible by remember { mutableStateOf(false) }
    var isLeaderboardsButtonVisible by remember { mutableStateOf(false) }
    var isTitleVisible by remember { mutableStateOf(false) }

    val categories = mapOf(
        1 to Pair("Guess the Fact", true),
        2 to Pair("Guess the Cat", false),
        3 to Pair("Left or Right", false)
    )

    LaunchedEffect(Unit) {
        isTitleVisible = true
        delay(500)
        isPlayButtonVisible = true
        delay(500)
        isLeaderboardsButtonVisible = true
    }


    
    Surface(
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                AnimatedVisibility(
                    visible = isTitleVisible,
                    enter = slideInVertically(
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow
                        )
                    )
                ) {
                    Text(
                        text = stringResource(id = R.string.app_name),
                        style = MaterialTheme.typography.displayLarge
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                AnimatedVisibility(
                    visible = isPlayButtonVisible,
                    enter = expandIn(
                        expandFrom = Alignment.Center,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow
                        )
                    )
                ) {
                    Button(
                        onClick = { onPlayButtonClicked()},
                        modifier = Modifier
                            .size(128.dp),
                        contentPadding = PaddingValues(16.dp),
                        shape = MaterialTheme.shapes.large,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                    {
                        Icon(
                            modifier = Modifier.fillMaxSize(),
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Play"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))


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
                    Button(
                        onClick = { onLeaderboardsButtonClicked()},
                        modifier = Modifier
                            .size(64.dp),
                        contentPadding = PaddingValues(16.dp),
                        shape = MaterialTheme.shapes.extraLarge,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary,
                            contentColor = MaterialTheme.colorScheme.onSecondary
                        )

                    ) {
                        Icon(
                            modifier = Modifier.fillMaxWidth(),
                            imageVector = Icons.Default.EmojiEvents,
                            contentDescription = "Leaderboards"
                        )
                    }
                }
            }

            if (state.categoriesMode) {
                    Dialog(onDismissRequest = { onCategoriesClosed() }) {
                        Box(
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.background)
                                .padding(8.dp)
                                .clip(RoundedCornerShape(16.dp))
                        ) { // Add this line
                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End,
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Button(
                                        onClick = { onCategoriesClosed() },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = MaterialTheme.colorScheme.secondary,
                                            contentColor = MaterialTheme.colorScheme.onSecondary
                                        ),
                                        contentPadding = PaddingValues(8.dp),
                                        modifier = Modifier.size(48.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "Close",
                                        )
                                    }
                                }
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "Pick a category:",
                                        style = MaterialTheme.typography.headlineSmall
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    categories.forEach { (id, categoryData) ->
                                        val (category, isAvailable) = categoryData
                                        Box {
                                            Button(
                                                onClick = {
                                                    onCategoriesClosed()
                                                    if (isAvailable) {
                                                        onCategoryPicked(id)
                                                    }
                                                },
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(8.dp),
                                                contentPadding = PaddingValues(16.dp),
                                                shape = MaterialTheme.shapes.large,
                                                colors = ButtonDefaults.buttonColors(
                                                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                                                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                                                ),
                                                enabled = isAvailable
                                            ) {
                                                Text(
                                                    category,
                                                    style = MaterialTheme.typography.bodyLarge
                                                )
                                            }
                                            if (!isAvailable) {
                                                Box(
                                                    modifier = Modifier
                                                        .align(Alignment.Center)
                                                        .rotate(15f)
                                                        .clip(RoundedCornerShape(4.dp))
                                                ) {
                                                    Box(
                                                        modifier = Modifier
                                                            .background(color = MaterialTheme.colorScheme.tertiary)

                                                            .padding(4.dp)
                                                    ) {
                                                        Text(
                                                            text = "Coming Soon",
                                                            style = MaterialTheme.typography.bodyLarge,
                                                            color = MaterialTheme.colorScheme.onTertiary
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
            }
        }
    )
}

@Preview
@Composable
fun PlayScreenPreview() {
    Surface {
        PlayScreen(
            state = PlayState(),
            onCategoryPicked = {},
            onCategoriesClosed = {},
            onLeaderboardsButtonClicked = {},
            onPlayButtonClicked = {},
        )
    }
}