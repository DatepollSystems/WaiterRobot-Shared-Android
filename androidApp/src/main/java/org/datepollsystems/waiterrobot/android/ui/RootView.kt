package org.datepollsystems.waiterrobot.android.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.dependency
import com.ramcosta.composedestinations.rememberNavHostEngine
import org.datepollsystems.waiterrobot.android.generated.navigation.NavGraphs
import org.datepollsystems.waiterrobot.android.generated.navigation.destinations.RootScreenDestination
import org.datepollsystems.waiterrobot.android.ui.core.LocalScaffoldState
import org.datepollsystems.waiterrobot.android.ui.core.handleSideEffects
import org.datepollsystems.waiterrobot.android.ui.core.theme.WaiterRobotTheme
import org.datepollsystems.waiterrobot.shared.features.settings.models.AppTheme
import org.datepollsystems.waiterrobot.shared.root.RootEffect
import org.datepollsystems.waiterrobot.shared.root.RootViewModel
import org.orbitmvi.orbit.compose.collectAsState

@Composable
fun RootView(vm: RootViewModel, onAppThemeChange: (AppTheme) -> Unit) {
    val navEngine = rememberNavHostEngine()
    val navController = navEngine.rememberNavController()
    val scaffoldState = rememberScaffoldState()

    val state = vm.collectAsState().value
    vm.handleSideEffects(navController) { handleSideEffects(it, scaffoldState) }

    val useDarkTheme = when (state.selectedTheme) {
        AppTheme.SYSTEM -> isSystemInDarkTheme()
        AppTheme.LIGHT -> false
        AppTheme.DARK -> true
    }

    LaunchedEffect(state.selectedTheme) { onAppThemeChange(state.selectedTheme) }

    WaiterRobotTheme(useDarkTheme) {
        CompositionLocalProvider(LocalScaffoldState provides scaffoldState) {
            DestinationsNavHost(
                navGraph = NavGraphs.root,
                engine = navEngine,
                navController = navController,
                dependenciesContainerBuilder = {
                    // Provide the viewModel also to the RootScreen
                    dependency(RootScreenDestination) {
                        dependency(vm)
                    }
                }
            )
        }
    }
}

private suspend fun handleSideEffects(
    effect: RootEffect,
    scaffoldState: ScaffoldState
) {
    when (effect) {
        is RootEffect.ShowSnackBar -> scaffoldState.snackbarHostState.showSnackbar(effect.message)
    }
}
