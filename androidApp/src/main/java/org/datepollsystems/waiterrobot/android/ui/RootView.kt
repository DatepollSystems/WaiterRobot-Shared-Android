package org.datepollsystems.waiterrobot.android.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.dependency
import com.ramcosta.composedestinations.rememberNavHostEngine
import org.datepollsystems.waiterrobot.android.generated.navigation.NavGraphs
import org.datepollsystems.waiterrobot.android.generated.navigation.destinations.RootScreenDestination
import org.datepollsystems.waiterrobot.android.ui.core.LocalSnackbarHostState
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
    val snackbarHostState = remember { SnackbarHostState() }
    val state by vm.collectAsState()
    vm.handleSideEffects(navController) { handleSideEffects(it, snackbarHostState) }

    val useDarkTheme = when (state.selectedTheme) {
        AppTheme.SYSTEM -> isSystemInDarkTheme()
        AppTheme.LIGHT -> false
        AppTheme.DARK -> true
    }

    LaunchedEffect(state.selectedTheme) { onAppThemeChange(state.selectedTheme) }

    WaiterRobotTheme(useDarkTheme) {
        CompositionLocalProvider(LocalSnackbarHostState provides snackbarHostState) {
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
    snackbarHostState: SnackbarHostState
) {
    when (effect) {
        is RootEffect.ShowSnackBar -> snackbarHostState.showSnackbar(effect.message)
    }
}
