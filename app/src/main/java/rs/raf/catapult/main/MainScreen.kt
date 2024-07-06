package rs.raf.catapult.main

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import rs.raf.catapult.core.compose.ErrorDialog
import rs.raf.catapult.core.compose.LoadingDialog
import rs.raf.catapult.navigation.AppNavigation
import rs.raf.catapult.navigation.navbar.AppNavbar
import rs.raf.catapult.user.datastore.UserStore


@Composable
fun MainScreen(
    userStore: UserStore,
    viewModel: MainScreenViewModel
){
    val navController = rememberNavController()

    val state = viewModel.state.collectAsState().value

    val curRoute by navController.currentBackStackEntryAsState()
    val route = curRoute?.destination?.route

    val view = LocalView.current
    val window = (view.context as Activity).window

    val insetsController = WindowCompat.getInsetsController(window, LocalView.current)

    if(!view.isInEditMode){
        if(route == "play/{categoryId}") {
            insetsController.apply {
                hide(WindowInsetsCompat.Type.systemBars())
                systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        }
        else {
            insetsController.apply {
                show(WindowInsetsCompat.Type.systemBars())
            }
        }
    }



    Scaffold(
        bottomBar = {

            if (route != "register" && route != "play/{categoryId}") {
                AppNavbar(navController = navController)
            }
        },
        content = { paddingValues ->


            Surface(modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()) {
                AppNavigation(navController, userStore)
                if (state.fetching) {
                    LoadingDialog()
                }
                state.error?.let { error ->
                    ErrorDialog(errorMessage = error.toString(),
                        onDismiss = { viewModel.setEvent(MainUiEvent.RetryClicked) }
                    )
                }
            }
        }
    )
}

