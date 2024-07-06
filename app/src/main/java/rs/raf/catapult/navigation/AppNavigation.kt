package rs.raf.catapult.navigation

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.activity
import androidx.navigation.compose.NavHost
import androidx.navigation.navArgument
import results
import rs.raf.catapult.breeds.details.breedDetails
import rs.raf.catapult.breeds.list.breedsList
import rs.raf.catapult.breeds.photos.gallery.photoGallery
import rs.raf.catapult.breeds.photos.grid.breedPhotosGrid
import rs.raf.catapult.leaderboard.publicleaderboard.leaderboard
import rs.raf.catapult.navigation.navbar.AppNavbarDestination
import rs.raf.catapult.quizz.play.play
import rs.raf.catapult.quizz.questions.questions
import rs.raf.catapult.user.datastore.UserStore
import rs.raf.catapult.user.profile.profile
import rs.raf.catapult.user.register.register


@Composable
fun AppNavigation(navController: NavHostController, userStore: UserStore) {

    val userData by userStore.userData.collectAsState()
    val context = LocalContext.current
    val activity = context as Activity

    NavHost(
        navController = navController,
        startDestination = if (userData.isEmpty()) "register" else AppNavbarDestination.Play.route,
    ) {
        register(
            route = "register",
            onRegisterButtonClick = {
                navController.navigate(AppNavbarDestination.Play.route)
            },
        )
        breedsList(
            route = AppNavbarDestination.Learn.route,
            onBreedClick = {
                navController.navigate("breeds/$it")
            }
        )
        breedDetails(
            route = "breeds/{id}",
            onGalleryClick = {
                navController.navigate("breeds/$it/images")
            },
            onClose = {
                navController.navigateUp()
            }
        )

        breedPhotosGrid(
            route = "breeds/{breedId}/images",
            arguments = listOf(
                navArgument(name = "breedId") {
                    nullable = false
                    type = NavType.StringType
                }
            ),
            onPhotoClick = {photoId, breedId ->
                navController.navigate("breeds/$breedId/images/$photoId")
            },
            onClose = {
                navController.popBackStack()
            }

        )

        photoGallery(
            route = "breeds/{breedId}/images/{photoId}",
            onClose = {
                navController.popBackStack()
            }
        )

        profile(
            route = AppNavbarDestination.Profile.route,

        )

        play(
            route = AppNavbarDestination.Play.route,
            onCategoryClick = {
                navController.navigate("play/{categoryId}")
            },
            onLeaderboardsButtonClicked = {
                navController.navigate("play/leaderboard")
            },
            onBack = {
                activity.finish()
            }
        )

        questions(
            route = "play/{categoryId}",
            onScoreShow = {
                navController.navigate("play/results")
            },
            onBack = {
                navController.popBackStack()

            }
        )

        results(
            route = "play/results",
            onShareClick = {
                navController.navigate("play/leaderboard")
            },
            onBackClick = {
                navController.navigate("play")
                navController.clearBackStack(0)
            }
        )

        leaderboard(
            route = "play/leaderboard",
            onBackClick = {
                navController.navigate("play")
            }
        )





    }

}