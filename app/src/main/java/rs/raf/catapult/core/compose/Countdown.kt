package rs.raf.catapult.core.compose

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.EaseInOutExpo
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import rs.raf.catapult.core.theme.CatapultTheme

@Composable
fun Countdown(
    modifier: Modifier = Modifier,
    canvasModifier: Modifier = Modifier.size(100.dp),
    textStyle: TextStyle,
    number: Int,
    max: Int,
) {
    val infiniteTransition = rememberInfiniteTransition(label = "infinite")

    val color by infiniteTransition.animateColor(
        initialValue = MaterialTheme.colorScheme.error,
        targetValue = MaterialTheme.colorScheme.onErrorContainer,
        animationSpec = infiniteRepeatable(
            animation = tween(500, easing = EaseInOutExpo),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "Test",
    )

    val sweepAngle = (number.toFloat() / max.toFloat()) * 360f

    val c2 = MaterialTheme.colorScheme.secondary
    remember { mutableStateOf(true) }


        Box(modifier = modifier, contentAlignment = Alignment.Center) {
            Canvas(modifier = canvasModifier) {
                drawCircle(
                    color = color,
                    radius = size.minDimension / 2
                )
                drawArc(
                    color = c2,
                    startAngle = -90f,
                    sweepAngle = -sweepAngle,
                    useCenter = true
                )
            }
            Text(
                text = "$number",
                style = textStyle,
                color = MaterialTheme.colorScheme.onPrimary,
            )
        }



}

@Preview
@Composable
fun CountdownPreview() {
    CatapultTheme{
        Countdown(
            modifier = Modifier.size(50.dp),
            number = 3,
            max = 10,
            textStyle = MaterialTheme.typography.headlineMedium
        )
    }

}