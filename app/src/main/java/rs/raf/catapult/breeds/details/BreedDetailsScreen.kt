package rs.raf.catapult.breeds.details

import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material.icons.filled.StarRate
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import rs.raf.catapult.R
import rs.raf.catapult.core.compose.AppIconButton
import com.example.catalist.core.compose.NoDataContent
import rs.raf.catapult.breeds.details.model.BreedDetailsUiModel
import rs.raf.catapult.breeds.details.model.BreedImageUiModel
import rs.raf.catapult.core.compose.PhotoPreview
import rs.raf.catapult.core.theme.CatapultTheme



fun NavGraphBuilder.breedDetails(
    route: String,
    onGalleryClick : (String) -> Unit,
    onClose: () -> Unit
) = composable(
    route = route,
) { navBackStackEntry ->
    val dataId = navBackStackEntry.arguments?.getString("id") // uzima id
        ?: throw IllegalArgumentException("id is required.") // ako ne postoji id

    val breedDetailsViewModel = hiltViewModel<BreedDetailsViewModel>(key=dataId)
    val state = breedDetailsViewModel.state.collectAsState()

    BreedDetailsScreen(
        state = state.value,
        eventPublisher = {
            breedDetailsViewModel.setEvent(it)
        },

        onClose = {
            onClose()
        },
        onGalleryClick = onGalleryClick,
        context = LocalContext.current
    )
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun BreedDetailsScreen(
    state: BreedDetailsState,
    eventPublisher: (DetailsUiEvent) -> Unit,
    onGalleryClick: (String) -> Unit,
    context: Context,
    onClose: () -> Unit,
) {

        Scaffold(
            topBar = {
                LargeTopAppBar(
                    windowInsets = WindowInsets(0.dp),
                    title = {
                        Column() {

                            Text(text = state.breed?.name ?: stringResource(id = R.string.loading))
                            //Spacer(modifier = Modifier.height(16.dp))
                            val altnames = (state.breed?.altNames
                                ?: "")
                            if (altnames.isNotBlank()) {
                                Text(
                                    modifier = Modifier
                                        .basicMarquee(),
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontStyle = FontStyle.Italic,
                                    text = stringResource(id = R.string.aka) + ": " + altnames
                                )
                            }
                        }
                    },

                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        scrolledContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    ),
                    actions = {
                        if (state.breed != null) {
                            GoToWebsiteAppIconButton(
                                onGoToWikiConfirmed = {
                                    eventPublisher(
                                        DetailsUiEvent.RequestGoToWebsite(
                                            context = context,
                                            url = state.breed.wikipediaUrl
                                        )
                                    )
                                }
                            )
                        }
                    },
                    navigationIcon = {
                        AppIconButton(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            onClick = onClose,
                        )
                    }
                )
            },
            content = { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    if (state.fetching) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center,
                        ) {
                            CircularProgressIndicator()
                        }
                    } else if (state.error != null) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center,
                        ) {
                            val errorMessage = when (state.error) {
                                is BreedDetailsState.DetailsError.DataUpdateFailed ->
                                    "Failed to load. Error message: ${state.error.cause?.message}."
                            }
                            Text(text = errorMessage)
                        }
                    } else if (state.breed != null && state.images != null) {
                        BreedDataColumn(
                            data = state.breed,
                            images = state.images,
                            state = state,
                            onGalleryClick = onGalleryClick,
                        )
                    } else {
                        NoDataContent(id = state.breedId)
                    }
                }
            }
        )
    }


@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun BreedDataColumn(
    data: BreedDetailsUiModel,
    images: List<BreedImageUiModel>,
    state: BreedDetailsState,
    onGalleryClick: (String) -> Unit,
) {
    val scrollState = rememberScrollState()
    Column (modifier = Modifier
        .padding(horizontal = 16.dp)
        .verticalScroll(scrollState),) {
        Spacer(modifier = Modifier.height(8.dp))


        Column {


            for (image in images) {
                Box(
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.extraLarge)
                        .background(color = MaterialTheme.colorScheme.primaryContainer)
                        .size(256.dp)
                ) {
                    PhotoPreview(
                        modifier = Modifier.fillMaxSize(),
                        url = image.url,
                    )
                }
            }
            Spacer(modifier = Modifier.size(4.dp))
            ElevatedButton(onClick = {
                onGalleryClick(state.breedId)
            }) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ){
                    Icon(imageVector = Icons.Default.PhotoLibrary, contentDescription = "Gallery")
                    Text(text = "Photo Gallery")
                }
            }
        }



        Spacer(modifier = Modifier.height(32.dp))

        Text(
            style = MaterialTheme.typography.titleMedium,
            text = stringResource(id = R.string.desc) +":"
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            style = MaterialTheme.typography.bodyMedium,
            text = data.description
        )

        Spacer(modifier = Modifier.height(32.dp))
        Text(
            style = MaterialTheme.typography.titleMedium,
            text = stringResource(id = R.string.origin) + ": "  + data.origin
        )
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            style = MaterialTheme.typography.titleMedium,
            text = stringResource(id = R.string.temperament) + ":"
        )
        Spacer(modifier = Modifier.height(8.dp))
        FlowRow(horizontalArrangement = Arrangement.spacedBy(7.dp)) {
            data.temperament.forEach {
                SuggestionChip(
                    onClick = {},
                    label = { Text(it) }
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))


        Text(
            style = MaterialTheme.typography.titleMedium,
            text = stringResource(id = R.string.lifespan) + ": "  + data.lifeSpan + " " + stringResource(id = R.string.years)
        )

        Spacer(modifier = Modifier.height(32.dp))
        Divider(thickness = 2.dp)
        Spacer(modifier = Modifier.height(32.dp))
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            val characteristics = listOf(
                Pair(stringResource(id = R.string.social_needs), data.socialNeeds),
                Pair(stringResource(id = R.string.shedding), data.sheddingLevel),
                Pair(stringResource(id = R.string.health), data.healthIssues),
                Pair(stringResource(id = R.string.child), data.childFriendly),
                Pair(stringResource(id = R.string.dog), data.dogFriendly),
                Pair(stringResource(id = R.string.adapt), data.adaptability),
                Pair(stringResource(id = R.string.affect), data.affectionLevel),
                Pair(stringResource(id = R.string.rare), data.rare)
            )

            characteristics.chunked(2).forEach { pair ->
                Row(horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier =  Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,) {
                    pair.forEach {
                        Column(horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.weight(1f),) {
                            Text(
                                style = MaterialTheme.typography.titleMedium,
                                text = it.first + ":"
                            )
                            if (it.first == stringResource(id = R.string.rare)) {
                                RarityIcon(rarity = it.second)
                            } else {
                                StarRating(rating = it.second)
                                Text(
                                    style = MaterialTheme.typography.labelMedium,
                                    text = it.second.toString() + "/" + "5"
                                )
                            }

                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
        Spacer(modifier = Modifier.height(48.dp))
    }

}

@Composable
fun RarityIcon(rarity: Int) {
    Icon(
        imageVector = if (rarity == 1) Icons.Default.Check else Icons.Default.Close,
        contentDescription = if (rarity == 1) stringResource(id = R.string.rare) else stringResource(id = R.string.not_rare)
    )
}

@Composable
private fun GoToWebsiteAppIconButton(
    onGoToWikiConfirmed:  () -> Unit,
) {
    var confirmationGoToWikiShown by remember { mutableStateOf(false) }
    if (confirmationGoToWikiShown) {
        AlertDialog(
            onDismissRequest = { confirmationGoToWikiShown = false },
            title = { stringResource(id = R.string.sure)},
            text = {
                Text(
                    text = stringResource(id = R.string.sure_leave)
                )
            },
            dismissButton = {
                TextButton(onClick = { confirmationGoToWikiShown = false }) {
                    Text(text = stringResource(id = R.string.no))
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    confirmationGoToWikiShown = false
                    onGoToWikiConfirmed()
                }) {
                    Text(text = stringResource(id = R.string.yes))
                }
            },
        )
    }

//    AppIconButton(
//        imageVector = Icons.Default.,
//        onClick = { confirmationDeleteShown = true },
//    )
    ElevatedButton(
        content= {
            Text(
            text = stringResource(id = R.string.wiki),
            )
        },
        onClick = { confirmationGoToWikiShown = true },
    )
}

@Composable
fun StarRating(rating: Int) {
    Row {
        for (i in 1..5) {
            Icon(
                imageVector = if (i <= rating) Icons.Default.StarRate else Icons.Default.StarOutline,
                contentDescription = null
            )
        }
    }
}

@Preview
@Composable
fun PreviewDetailsScreen() {
    CatapultTheme {

    Surface {
        BreedDetailsScreen(
            state = BreedDetailsState(
                breedId = "124124",
                breed = BreedDetailsUiModel(
                    id = "1",
                    name = "Macka",
                    altNames = "mala, cao, cao, mala, cao, cao, mala, cao, cao, mala, cao, cao",
                    description = "Ovo je mala maca",
                    temperament = listOf("mala", "straftasta", "slatka", "nice", "cool"),
                    wikipediaUrl = "www.wikipedia.com",
                    socialNeeds = 4,
                    sheddingLevel = 3,
                    rare = 0,
                    origin = "Serbia",
                    lifeSpan = "22 - 35",
                    healthIssues = 2,
                    dogFriendly = 1,
                    childFriendly = 3,
                    avgWeightImp = 22.5,
                    avgWeightMet = 33.6,
                    affectionLevel = 5,
                    adaptability = 5

                ),
                images = listOf(BreedImageUiModel(id="gCEFbK7in", url = "https://cdn2.thecatapi.com/images/gCEFbK7in.jpg"))
            ),
            eventPublisher = {},
         //   onEditClick = {},
            onClose = {},
            context = LocalContext.current,
            onGalleryClick = {}
        )
    }}
}