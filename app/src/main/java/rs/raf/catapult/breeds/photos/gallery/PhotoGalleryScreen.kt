package rs.raf.catapult.breeds.photos.gallery

import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import rs.raf.catapult.core.compose.AppIconButton
import rs.raf.catapult.core.compose.PhotoPreview

fun NavGraphBuilder.photoGallery(
    route: String,
    onClose: () -> Unit,
) = composable(
    route = route,
    enterTransition = { slideInVertically { it } },
    popExitTransition = { slideOutVertically { it } },
) { navBackStackEntry ->

    val photoGalleryViewModel = hiltViewModel<PhotoGalleryViewModel>(navBackStackEntry)


    val state = photoGalleryViewModel.state.collectAsState()

    if(state.value.photos.isNotEmpty() && state.value.photoId.isNotEmpty()) {
        PhotoGalleryScreen(
            state = state.value,
            onClose = onClose,
        )
    }

}


@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PhotoGalleryScreen(
    state: PhotoGalleryContract.PhotoGalleryUiState,
    onClose: () -> Unit,
) {
    val initialPage = state.photos.indexOfFirst { it.photoId == state.photoId }

    val pagerState = rememberPagerState(
        pageCount = {
            state.photos.size
        },
        initialPage = initialPage
    )

    val currentTitle by remember { mutableStateOf("") }


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                windowInsets = WindowInsets(0.dp),
                title = {
                    Text(
                        text = currentTitle,
                        textAlign = TextAlign.Center,
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
            if (state.photos.isNotEmpty()) {

                LaunchedEffect(pagerState) {
                    snapshotFlow { pagerState.currentPage }.collect { pageIndex ->
                        val album = state.photos.getOrNull(pageIndex)
                       // currentTitle = album?.title ?: ""
                    }
                }

                HorizontalPager(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = paddingValues,
                    pageSize = PageSize.Fill,
                    pageSpacing = 16.dp,
                    state = pagerState,
                    key = { state.photos[it].photoId },
                ) { pageIndex ->
                    val photo = state.photos[pageIndex]
                    PhotoPreview(
                        modifier = Modifier,
                        url = photo.url,
                        isPager = true
                       // title = photo.title,
                       // showTitle = false,
                    )
                }

            } else {
                Text(
                    modifier = Modifier.fillMaxSize(),
                    text = "No albums.",
                )
            }
        },
    )
}