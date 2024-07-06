package rs.raf.catapult.navigation.navbar

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode.Companion.Screen
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import rs.raf.catapult.navigation.AppNavigation
import javax.inject.Inject


@Composable
fun AppNavbar(
    navController: NavHostController,
) {
    val items = listOf(
        AppNavbarDestination.Learn,
        AppNavbarDestination.Play,
        AppNavbarDestination.Profile
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar(
    ) {


        items.forEach { item ->
            AddItem(
                item = item,
                currentDestination = currentDestination,
                navController = navController
            )
        }
    }
}


@Composable
fun RowScope.AddItem(
    item: AppNavbarDestination,
    currentDestination: NavDestination?,
    navController: NavHostController
) {
   NavigationBarItem(
         icon = { Icon(item.icon, contentDescription = null) },
         label = { Text(item.label) },
         selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
       onClick = {
           navController.navigate(item.route) {
               popUpTo(navController.graph.findStartDestination().id)
               launchSingleTop = true
           }
       }
   )
}
