package rs.raf.catapult.leaderboard.publicleaderboard

import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import rs.raf.catapult.core.compose.AppIconButton
import kotlinx.coroutines.launch
import rs.raf.catapult.core.theme.CatapultTheme
import rs.raf.catapult.leaderboard.publicleaderboard.model.LeaderboardElementUiModel
import java.util.Date

fun NavGraphBuilder.leaderboard(
    route: String,
    onBackClick: () -> Unit = {},
) = composable(
    route = route,
    enterTransition = { slideInVertically { it } },
    popExitTransition = { slideOutVertically { it } },
) { navBackStackEntry ->

    val viewModel: LeaderboardViewModel = hiltViewModel(navBackStackEntry)
    val state by viewModel.state.collectAsState()

    LeaderboardContent(state, leaderboard = state.leaderboard, onBackClick = onBackClick)
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaderboardContent(
    state: LeaderboardState,
    leaderboard: List<LeaderboardElementUiModel>,
    onBackClick: () -> Unit = {},
) {
    val listState = rememberLazyListState()
    val showScrollToTop by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 0
        }
    }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            MediumTopAppBar(
                windowInsets = WindowInsets(0.dp),
                title =
                {  Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                    Icon(modifier=Modifier.size(32.dp), imageVector = Icons.Default.EmojiEvents, contentDescription = "Leaderboard")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Leaderboard",
                        style = MaterialTheme.typography.displaySmall
                    )
                }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    scrolledContainerColor = MaterialTheme.colorScheme.primaryContainer,
                ),
                navigationIcon = {
                    AppIconButton(
                        imageVector = Icons.Default.ArrowBackIosNew,
                        onClick = onBackClick,
                    )
                }
            )
        }
    ) { paddingValues ->

        if (state.fetching) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        } else if (state.error != null) {
            Text(text = state.error.toString(), color = MaterialTheme.colorScheme.error)
        } else {

            Box(modifier = Modifier.fillMaxSize()) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = paddingValues
                ) {
                    items(leaderboard) { item ->
                        LeaderboardElement(leaderboardElement = item)
                        Divider()
                    }
                    item {
                        Spacer(modifier = Modifier.height(64.dp))
                    }
                }



                if (showScrollToTop) {
                    FloatingActionButton(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(12.dp),
                        onClick = {
                            coroutineScope.launch {
                                listState.animateScrollToItem(0)
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowUp,
                            contentDescription = "Scroll to top"
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LeaderboardElement(
    leaderboardElement: LeaderboardElementUiModel,
) {
    ListItem(
        headlineContent = {
            Column() {


                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        leaderboardElement.nickname,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "Total Quizzes: " + leaderboardElement.totalQuizzes.toString(),
                        style = MaterialTheme.typography.labelMedium,
                    )
                }


                Column() {
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth(),
                        progress = leaderboardElement.score.toFloat() / 100,
                    )
                    Text(
                        modifier = Modifier.align(Alignment.End),
                        text= String.format("Score: %.2f", leaderboardElement.score),
                        style = MaterialTheme.typography.bodyMedium
                    )

                }
            }
                          },
        leadingContent = {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ){
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .border(
                            3.dp,
                            MaterialTheme.colorScheme.primary,
                            shape = MaterialTheme.shapes.extraLarge
                        )

                ) {
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = leaderboardElement.id.toString(),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
                Spacer(modifier = Modifier.size(8.dp))
                Icon(
                    modifier = Modifier.size(48.dp),
                    tint = MaterialTheme.colorScheme.secondary,
                    imageVector = Icons.Filled.AccountBox,
                    contentDescription = "Localized description",
                )
            }
        }
    )

}

@Preview
@Composable
fun LeaderboardScreenPreview() {
    CatapultTheme {
        LeaderboardContent(
            state = LeaderboardState(),
            leaderboard = listOf(
                LeaderboardElementUiModel(
                    id = 1,
                    nickname = "Nickname1",
                    createdAt = Date(),
                    score = 100.0,
                    totalQuizzes = 102

                ),
                LeaderboardElementUiModel(
                    id = 2,
                    nickname = "Nickname2",
                    createdAt = Date(),
                    score = 65.32,
                    totalQuizzes = 25
                ),
                LeaderboardElementUiModel(
                    id = 125,
                    nickname = "Nickname3",
                    createdAt = Date(),
                    score = 44.1245,
                    totalQuizzes = 5
                ),
            )
        )
    }

}