package org.datepollsystems.waiterrobot.android.ui

import android.content.Context
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.rememberNavHostEngine
import org.datepollsystems.waiterrobot.android.generated.navigation.NavGraphs
import org.datepollsystems.waiterrobot.android.ui.core.LocalSnackbarHostState
import org.datepollsystems.waiterrobot.android.ui.core.direction
import org.datepollsystems.waiterrobot.android.ui.core.handleSideEffects
import org.datepollsystems.waiterrobot.android.ui.core.theme.WaiterRobotTheme
import org.datepollsystems.waiterrobot.shared.core.CommonApp
import org.datepollsystems.waiterrobot.shared.features.settings.models.AppTheme
import org.datepollsystems.waiterrobot.shared.root.presentation.RootEffect
import org.datepollsystems.waiterrobot.shared.root.presentation.RootViewModel
import org.orbitmvi.orbit.compose.collectAsState

@Composable
fun RootView(vm: RootViewModel, onAppThemeChange: (AppTheme) -> Unit) {
    val navEngine = rememberNavHostEngine()
    val navController = navEngine.rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val state by vm.collectAsState()
    val context = LocalContext.current
    vm.handleSideEffects(navController) { handleSideEffects(it, snackbarHostState, context) }

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
                start = remember { CommonApp.getNextRootScreen().direction }
            )
        }
    }
}

private suspend fun handleSideEffects(
    effect: RootEffect,
    snackbarHostState: SnackbarHostState,
    context: Context,
) {
    when (effect) {
        is RootEffect.ShowSnackBar -> snackbarHostState.showSnackbar(effect.message.toString(context))
    }
}
