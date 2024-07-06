import androidx.activity.compose.BackHandler
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import rs.raf.catapult.core.compose.QuizzResults
import rs.raf.catapult.quizz.results.ResultsUiEvent
import rs.raf.catapult.quizz.results.ResultsViewModel

fun NavGraphBuilder.results(
    route: String,
    onShareClick: () -> Unit,
    onBackClick: () -> Unit,
){

    composable(
        route = route,
    ) {

        val viewModel: ResultsViewModel = hiltViewModel()
        val state = viewModel.state.collectAsState().value

        BackHandler {
            onBackClick()
        }


        QuizzResults(
            score = state.score,
            onShareClick = {
                viewModel.setEvent(ResultsUiEvent.OnShareClick(doRedirect = onShareClick))
            },
            error = state.error,
            onBackClick = onBackClick
        )
    }
}