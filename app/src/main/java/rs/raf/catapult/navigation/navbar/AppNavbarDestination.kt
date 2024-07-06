package rs.raf.catapult.navigation.navbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.School
import androidx.compose.ui.graphics.vector.ImageVector

sealed class AppNavbarDestination(val route: String, val icon: ImageVector, val label:String) {
    data object Learn : AppNavbarDestination("breeds", Icons.Default.School, "Learn")
    data object Play : AppNavbarDestination("play", Icons.Default.PlayCircle, "Play")
    data object Profile : AppNavbarDestination("profile", Icons.Default.ManageAccounts, "Profile")
}