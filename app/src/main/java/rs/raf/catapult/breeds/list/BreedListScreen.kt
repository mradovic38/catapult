package rs.raf.catapult.breeds.list

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SuggestionChip
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import rs.raf.catapult.core.compose.AppIconButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import rs.raf.catapult.R
import rs.raf.catapult.breeds.list.model.BreedListElementUiModel
import rs.raf.catapult.breeds.repository.SampleData
import rs.raf.catapult.core.theme.CatapultTheme


fun NavGraphBuilder.breedsList(
    route: String,
    onBreedClick : (String) -> Unit,
) = composable(route = route) {
    navBackStackEntry ->
    val breedListViewModel : BreedListViewModel = hiltViewModel(navBackStackEntry)//viewModel<BreedListViewModel>() // uzimamo view model (BreedListViewModel nasledjuje ViewModel)
    val state by breedListViewModel.state.collectAsState() // referenca na read-only state




    BreedListScreen(
        state = state,
        onItemClick = {// sta da se desi nakon klika na element (idemo na detalje)
            onBreedClick(it.id)
        },
        eventPublisher = {
            breedListViewModel.setEvent(it)
        },
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BreedListScreen(
    state: BreedListState,
    onItemClick: (BreedListElementUiModel) -> Unit,
    eventPublisher: (ListUiEvent) -> Unit,
) {
    val uiScope = rememberCoroutineScope()


    BackHandler(enabled = state.query.isNotEmpty()) {
        eventPublisher(ListUiEvent.SearchQueryChanged(query = ""))
        eventPublisher(ListUiEvent.SearchClicked(query = ""))
        eventPublisher(ListUiEvent.CloseSearchMode)
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {

            Column() {
                CenterAlignedTopAppBar(
                    title = {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                            Icon(modifier=Modifier.size(32.dp), imageVector = Icons.Default.School, contentDescription = "Learn")
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Learn",
                                style = MaterialTheme.typography.displaySmall
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        scrolledContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    ),
                    windowInsets = WindowInsets(0.dp),
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    SearchBar(
                        windowInsets = WindowInsets(0.dp),
                        placeholder = {
                            Text(
                                text = "Search...",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontStyle = FontStyle.Italic,
                                fontWeight = FontWeight(300)
                            )

                        },
                        modifier = Modifier.padding(all = 8.dp),
                        query = state.query,
                        onQueryChange = { query ->
                            eventPublisher(ListUiEvent.SearchQueryChanged(query = query))
                        },
                        onSearch = { query ->
                            eventPublisher(ListUiEvent.SearchClicked(query = query))
                            eventPublisher(ListUiEvent.CloseSearchMode)
                        },
                        active = state.isSearchMode,
                        onActiveChange = { active ->
                            eventPublisher(ListUiEvent.ChangeSearchBarActive(active))
                            Log.d("ACTIVE STATUS", active.toString())
                        },
                        colors = SearchBarDefaults.colors(
                            containerColor = MaterialTheme.colorScheme.background,
                        ),
                        leadingIcon = {
                            AppIconButton(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                onClick = {
                                    eventPublisher(ListUiEvent.SearchClicked(query = state.query))
                                    eventPublisher(ListUiEvent.CloseSearchMode)
                                }
                            )
                        },

                        trailingIcon = {
                            if (state.query.isNotBlank()) {
                                AppIconButton(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = null,
                                    onClick = {
                                        eventPublisher(ListUiEvent.SearchQueryChanged(query = ""))
                                        eventPublisher(ListUiEvent.SearchClicked(query = ""))
                                        eventPublisher(ListUiEvent.CloseSearchMode)
                                    }
                                )
                            }
                        },

                        content = {}
                    )

                }
            }


        },
        content = { paddingValues ->
            // ako je prazna lista -> loading indikator
            if (state.fetching) { // stanje fetching
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator()  // loading indikator
                }
            }

            else if (state.breeds.isEmpty()){ // nije u stanju fetching
                if (state.error != null) { // doslo do greske -> napisi gresku
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        val errorMessage = when (state.error) {
                            is BreedListState.ListError.ListUpdateFailed ->
                                stringResource(id = R.string.failed_load) + ": ${state.error.cause?.message}."

                            else -> {
                                stringResource(id = R.string.failed_load)
                            }
                        }
                        Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
                    }
                } else {  // lista je prazna -> napisi da je prazna
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(text = stringResource(id = R.string.no_breeds))
                    }
                }
            }

            else{
                val listState = rememberLazyListState()
                val showScrollToTop by remember {
                    derivedStateOf {
                        val show = listState.firstVisibleItemIndex > 0
                        Log.d("DEBUG", "showScrollToTop: $show")
                        show
                    }
                }

                BreedList(
                    items = state.breeds,
                    onItemClick = onItemClick,
                    paddingValues = paddingValues,
                    listState = listState,
                    showScrollToTop = showScrollToTop,
                    uiScope = uiScope
                )
            }


        }
    )
}

@Composable
private fun BreedList(
    items: List<BreedListElementUiModel>,
    onItemClick: (BreedListElementUiModel) -> Unit,
    paddingValues: PaddingValues,
    listState: LazyListState,
    showScrollToTop: Boolean,
    uiScope: CoroutineScope,
) {

    Box(modifier = Modifier
        .fillMaxSize()
        .padding(vertical = 8.dp)) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = paddingValues,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(items) { item ->
                BreedListItem(
                    data = item,
                    onClick = { onItemClick(item) }
                )
            }
        }

        if (showScrollToTop) {
            FloatingActionButton(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(12.dp),
                onClick = {
                    uiScope.launch { listState.animateScrollToItem(index = 0) }
                },
            ) {
                Image(imageVector = Icons.Default.KeyboardArrowUp, contentDescription = "Scroll to Top")
            }
        }
    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun BreedListItem(
    data: BreedListElementUiModel,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .clickable {
                onClick()
            },
    ) {
        Row(modifier = Modifier
            .padding(all = 16.dp)
            .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Column {

                Text(
                    //modifier = Modifier.padding(all = 16.dp),
                    text = data.name,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                if(data.altNames.isNotEmpty()){
                    Text(
                        //                modifier = Modifier
                        //                    .padding(horizontal = 16.dp)
                        //                    .padding(bottom = 16.dp)
                        //                    .weight(weight = 1f),
                        fontStyle = FontStyle.Italic,
                        style = MaterialTheme.typography.bodySmall,
                        text = data.altNames
                    )
                }
            }
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.End
            ) {
                Icon(
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp, top = 8.dp),
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = null
                )
            }

        }
        Row(modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)){
            Column {
                Text(

                    text = data.description,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
        Row(modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom=16.dp)){
            FlowRow(horizontalArrangement = Arrangement.spacedBy(7.dp)) {
                data.temperament.forEach {
                    SuggestionChip(
                        onClick = {},
                        label = { Text(it) }
                    )
                }
            }

        }



    }
}



@Preview
@Composable
fun PreviewBreedListScreen() {
    CatapultTheme {
        BreedListScreen(
            state = BreedListState(breeds = SampleData),
            onItemClick = {},
            eventPublisher = {},
        )
    }
}
