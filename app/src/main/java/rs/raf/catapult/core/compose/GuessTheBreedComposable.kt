package rs.raf.catapult.core.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandIn
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import rs.raf.catapult.core.theme.CatapultTheme

@Composable
fun GuessTheBreedComposable (
    questionIdx: Int = 14,
    text: String = "Guess the Cat Breed",
    textColor : Color = MaterialTheme.colorScheme.onSecondary,
    textBoxColor: Color = MaterialTheme.colorScheme.secondary,
    url: String,
    options: List<String>,
    onOptionClick:(String) -> Unit,
){
    Scaffold {paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ){
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(horizontal = 8.dp),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val questionVisible = remember { mutableStateOf(false) }
                val imageVisible = remember { mutableStateOf(false) }
                LaunchedEffect(UInt) {
                    delay(0)
                    questionVisible.value = true
                    delay(200L)
                    imageVisible.value = true
                }
                AnimatedVisibility(
                    visible = imageVisible.value,

                    enter = expandIn(
                        expandFrom = Alignment.Center,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow
                        )),
                    exit = shrinkOut(
                        shrinkTowards = Alignment.Center,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow
                        )
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(MaterialTheme.shapes.medium)
                                .background(color = textBoxColor)
                                .padding(8.dp),

                            ) {
                            Text(
                                text = "$questionIdx",
                                style = MaterialTheme.typography.headlineSmall,
                                textAlign = TextAlign.Center,
                                color = textColor,
                                modifier = Modifier
                            )
                        }
                        Spacer(modifier =  Modifier.size(4.dp))
                        Box(
                            modifier = Modifier
                                .clip(MaterialTheme.shapes.extraLarge)
                                .background(color = MaterialTheme.colorScheme.primaryContainer)
                                .size(256.dp)
                        ) {
                            PhotoPreview(
                                modifier = Modifier.fillMaxSize(),
                                url = url,
                            )

                        }
                        Spacer(modifier =  Modifier.size(4.dp))
                        Box(
                            modifier = Modifier
                                .clip(MaterialTheme.shapes.medium)
                                .background(color = textBoxColor)
                                .padding(8.dp),

                        ) {
                            Text(
                                text = text,
                                style = MaterialTheme.typography.titleLarge,
                                textAlign = TextAlign.Center,
                                color = textColor,
                                modifier = Modifier
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.size(16.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = (MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                            shape = MaterialTheme.shapes.medium
                        ),
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    options.forEachIndexed { index, breedName ->
                        val buttonVisible = remember { mutableStateOf(false) }
                        LaunchedEffect(key1=index) {
                            buttonVisible.value = true
                        }
                        AnimatedVisibility(
                            visible = buttonVisible.value,
                            enter = expandIn(
                                expandFrom = Alignment.Center,
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioMediumBouncy,
                                    stiffness = Spring.StiffnessLow
                                )
                            )
                        ) {
                            Button(
                                onClick = {
                                    onOptionClick(breedName)
                                },
                                modifier = Modifier
                                    .padding(8.dp)
                                    .fillMaxWidth(),
                            ) {
                                Text(text = breedName, style = MaterialTheme.typography.bodyLarge)
                            }
                        }
                    }
                }
            }
        }

    }
}

@Preview
@Composable
fun GuessTheBreedComposablePreview(){
    CatapultTheme {
        GuessTheBreedComposable(
            url = "https://i.natgeofe.com/n/548467d8-c5f1-4551-9f58-6817a8d2c45e/NationalGeographic_2572187_square.jpg",
            options = listOf("Abyssinian", "Aegean", "American Bobtail", "American Curl"),
            onOptionClick = {}
        )
    }

}