package rs.raf.catapult.breeds.photos.grid

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import rs.raf.catapult.core.compose.AppIconButton
import rs.raf.catapult.core.compose.PhotoPreview
import rs.raf.catapult.breeds.photos.grid.model.PhotoUiModel

fun NavGraphBuilder.breedPhotosGrid(
    route: String,
    arguments: List<NamedNavArgument>,
    onPhotoClick: (String, String) -> Unit,
    onClose: () -> Unit,
) = composable(
    route = route,
    arguments = arguments,
) { navBackStackEntry ->

    val albumGridViewModel: PhotoGridViewModel = hiltViewModel(navBackStackEntry)

    val state = albumGridViewModel.state.collectAsState()

    PhotoGridScreen(
        state = state.value,
        onPhotoClick = onPhotoClick,
        onClose = onClose,
    )


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoGridScreen(
    state: PhotoGridContract.PhotoGridUiState,
    onPhotoClick: (photoId: String, breedId: String) -> Unit,
    onClose: () -> Unit,
) {


    if(state.error.isNotEmpty() || state.breed == null){
        Text(
            text = state.error,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxSize(),
        )
    }
    else if(state.updating || state.gettingBreed){
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator()  // loading indikator
        }
    }
    else if (state.photos.isEmpty()){
        Text(
            text = "There are no photos to show.",
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxSize(),
        )
    }

    else {

            Scaffold(
                topBar = {
                    MediumTopAppBar(
                        windowInsets = WindowInsets(0.dp),
                        title = {
                            Text(
                                text = state.breed.name,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
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
                    BoxWithConstraints(
                        modifier = Modifier,
                        contentAlignment = Alignment.BottomCenter,
                    ) {
                        val screenWidth = this.maxWidth
                        val cellSize = (screenWidth / 2) - 4.dp

                        LazyVerticalGrid(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 4.dp),
                            columns = GridCells.Fixed(2),
                            contentPadding = paddingValues,
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                        ) {

                            itemsIndexed(
                                items = state.photos,
                                key = { index: Int, photo: PhotoUiModel -> photo.id },
                            ) { index: Int, photo: PhotoUiModel ->
                                Card(
                                    modifier = Modifier
                                        .size(cellSize)
                                        .clickable {
                                            onPhotoClick(photo.id, state.breed.id)
                                        },
                                ) {
                                    PhotoPreview(
                                        modifier = Modifier.fillMaxSize(),
                                        url = photo.photoUrl,
                                        isPager = false
                                        //title = photo.title
                                    )
                                }
                            }



                            item(
                                span = {
                                    GridItemSpan(2)
                                }
                            ) {
                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(all = 32.dp),
                                    text = "You have reached the end" + // TODO: String R
                                            "\n\uD83D\uDC9A",
                                    textAlign = TextAlign.Center,
                                )
                            }

                        }
                    }
                },
            )
        }

}

