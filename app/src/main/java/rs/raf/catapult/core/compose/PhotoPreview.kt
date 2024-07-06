package rs.raf.catapult.core.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage

@Composable
fun PhotoPreview(
    modifier: Modifier,
    url: String?,
   // title: String,
    isPager: Boolean = false,
) {
    Box(modifier = modifier, contentAlignment = Alignment.BottomCenter) {
        SubcomposeAsyncImage(
            modifier = Modifier.fillMaxSize(),
            model = url,
            loading = {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(36.dp),
                    )
                }

            },
            contentDescription = null,
            contentScale = if(isPager) ContentScale.Fit else ContentScale.Crop,
        )

//        if (showTitle) { // TODO: Dodati da je opis slike title
//            Text(
//                modifier = Modifier
//                    .background(color = Color.Black.copy(alpha = 0.5f))
//                    .fillMaxWidth()
//                    .padding(all = 8.dp),
//                text = title,
//                textAlign = TextAlign.Center,
//                maxLines = 1,
//                overflow = TextOverflow.Ellipsis,
//                color = Color.White,
//            )
//        }
    }
}