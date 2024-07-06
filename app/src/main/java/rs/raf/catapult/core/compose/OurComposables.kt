package rs.raf.catapult.core.compose

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun AppIconButton(
    imageVector: ImageVector,
    onClick: () -> Unit,
    contentDescription: String? = null,
    tint: Color = LocalContentColor.current,
) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
            tint = tint,
        )
    }
}

@Composable
fun AppDropdownMenuItem(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit,
) {
    DropdownMenuItem(
        leadingIcon = { Icon(imageVector = icon, contentDescription = text) },
        text = { Text(text = text) },
        onClick = onClick,
    )
}

@Composable
fun BreedOutlinedTextField(
    modifier: Modifier,
    breed: String,
    onBreedChange: (String) -> Unit
) {
    var breedVisible by remember { mutableStateOf(false) }
    OutlinedTextField(
        modifier = modifier,
        value = breed,
        onValueChange = onBreedChange,
        trailingIcon = {
            AppIconButton(imageVector = if (breedVisible) {
                Icons.Outlined.Warning
            } else {
                Icons.Outlined.Lock
            }, onClick = {
                breedVisible = !breedVisible
            })
        },
        placeholder = { Text(text = "Breed") },
        label = { Text(text = "Breed") },
        //visualTransformation = if (breedVisible) {
        visualTransformation =  VisualTransformation.None)
        } //else {
            //BreedVisualTransformation()
        //}
//}

