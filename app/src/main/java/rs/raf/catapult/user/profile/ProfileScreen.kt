package rs.raf.catapult.user.profile

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.coroutines.launch
import rs.raf.catapult.core.theme.CatapultTheme
import rs.raf.catapult.user.datastore.UserStore
import rs.raf.catapult.user.profile.model.ResultUiModel

fun NavGraphBuilder.profile(
    route: String,
) = composable(
    route = route
    ) {navBackStackEntry ->
    val viewModel = hiltViewModel<ProfileViewModel>(navBackStackEntry)
    val state by viewModel.state.collectAsState()

    ProfileScreen(
        state = state,
        onEditClick = { viewModel.setEvent(ProfileUiEvent.OnEditClick) },
        onConfirmClick = { email, nickname, firstName, lastName ->
            viewModel.setEvent(ProfileUiEvent.OnConfirmClick(email, nickname, firstName, lastName))
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(state: ProfileState,
                  onEditClick: () -> Unit,
                  onConfirmClick: (String, String, String, String) -> Unit) {


    var nickname by rememberSaveable { mutableStateOf(state.nickname) }
    var firstName by rememberSaveable { mutableStateOf(state.firstName) }
    var lastName by rememberSaveable { mutableStateOf(state.lastName) }
    var email by rememberSaveable { mutableStateOf(state.email)}

    if (state.editMode) {
        nickname = state.nickname
        firstName = state.firstName
        lastName = state.lastName
        email = state.email
    }

    BackHandler(enabled = state.editMode) {
        onEditClick()
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            TopAppBar(
                windowInsets = WindowInsets(0.dp),
                title = { Text(text = if (state.editMode) "Edit Profile" else "Profile") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    scrolledContainerColor = MaterialTheme.colorScheme.primaryContainer,
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onEditClick() },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                ) {
                if (!state.editMode) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Cancel,
                        contentDescription = "Cancel",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                horizontalAlignment = Alignment.Start
            ) {
                AnimatedVisibility(
                    visible = state.editMode,
                    enter = slideInVertically(initialOffsetY = {-it}, animationSpec =  tween(1000) ),
                    exit =  fadeOut() + slideOutVertically(targetOffsetY = {-it}, animationSpec =  tween(1000)),
                ) {
                    Column {
                    OutlinedTextField(
                        value = nickname,
                        onValueChange = { nickname = it },
                        label = { Text("Nickname") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = firstName,
                        onValueChange = { firstName = it },
                        label = { Text("First Name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = lastName,
                        onValueChange = { lastName = it },
                        label = { Text("Last Name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    if (state.error.isNotEmpty()) {
                        Text(text = state.error, color = MaterialTheme.colorScheme.error)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    Button(onClick = {
                        onConfirmClick(email, nickname, firstName, lastName)
                    }) {
                        Text("Confirm")
                    }
                }}
                }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                horizontalAlignment = Alignment.Start
            ) {
                AnimatedVisibility(
                    visible = !state.editMode,
                    enter = slideInVertically(initialOffsetY = {it}, animationSpec =  tween(1000) ),
                    exit = fadeOut() + slideOutVertically(targetOffsetY = {it}, animationSpec =  tween(1000)),
                ) {
                    Column {
                        Text(text = "Nickname:", style = MaterialTheme.typography.titleLarge)
                        Text(text = state.nickname, style = MaterialTheme.typography.bodyLarge)
                        Text(text = "First Name:", style = MaterialTheme.typography.titleLarge)
                        Text(text = state.firstName, style = MaterialTheme.typography.bodyLarge)
                        Text(text = "Last Name:", style = MaterialTheme.typography.titleLarge)
                        Text(text = state.lastName, style = MaterialTheme.typography.bodyLarge)
                        Text(text = "E-Mail:", style = MaterialTheme.typography.titleLarge)
                        Text(text = state.email, style = MaterialTheme.typography.bodyLarge)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Best Ranking:", style = MaterialTheme.typography.titleLarge)
                        Text(text = state.bestRanking.toString() + " (Score: " + state.bestScore.toString() + ")", style = MaterialTheme.typography.bodyLarge)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = "My Scores:", style = MaterialTheme.typography.titleLarge)
                        if(state.resutsError.isNotEmpty()) {
                            Text(text = state.resutsError, color = MaterialTheme.colorScheme.error)
                        } else {
                            Spacer(modifier = Modifier.height(8.dp))
                            if(state.fetching){
                                Box(modifier = Modifier.fillMaxSize()) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.align(Alignment.Center)
                                    )
                                }
                            }
                            else{
                                ResultsContent(results = state.allResults)
                            }

                        }
                    }
                }
            }
        }



    )
}


@Composable
fun ResultsContent(
    results: List<ResultUiModel>,
) {
    val listState = rememberLazyListState()
    val showScrollToTop by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 0
        }
    }
    val coroutineScope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
        ) {
            items(results) { item ->
                ResultsElement(item)
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
                        .padding(bottom = 64.dp),
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


@Composable
fun ResultsElement(
    result: ResultUiModel,
) {
    ListItem(
        headlineContent = {
            Column() {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier.fillMaxWidth()
                ) {


                    Column() {
                        LinearProgressIndicator(
                            modifier = Modifier.fillMaxWidth(),
                            progress = result.score.toFloat() / 100,
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = String.format("%.2f", result.score),
                                style = MaterialTheme.typography.bodyMedium
                            )
                            if (result.isPublic) {
                                Icon(
                                    imageVector = Icons.Filled.EmojiEvents,
                                    contentDescription = "Public",
                                    tint = MaterialTheme.colorScheme.secondary
                                )
                            }
                        }


                    }
                }
            }
        })


}

@Preview
@Composable
fun ProfileScreenPreview() {
    CatapultTheme {
        ResultsContent(
            results = listOf(
                ResultUiModel(1, true, 100.0),
                ResultUiModel(2, false, 50.0),
                ResultUiModel(3, true, 75.0),
                ResultUiModel(4, false, 25.0),
                ResultUiModel(5, true, 90.0),
                ResultUiModel(6, false, 10.0),
                ResultUiModel(7, true, 85.0),
                ResultUiModel(8, false, 15.0),
                ResultUiModel(9, true, 95.0),
                ResultUiModel(10, false, 5.0),
            )
        )

    }

}