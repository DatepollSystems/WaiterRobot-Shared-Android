package org.datepollsystems.waiterrobot.android

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate.*
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.LaunchedEffect
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import co.touchlab.kermit.Logger
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.dependency
import com.ramcosta.composedestinations.rememberNavHostEngine
import org.datepollsystems.waiterrobot.android.generated.navigation.NavGraphs
import org.datepollsystems.waiterrobot.android.generated.navigation.destinations.RootScreenDestination
import org.datepollsystems.waiterrobot.android.ui.core.handleNavAction
import org.datepollsystems.waiterrobot.android.ui.core.theme.WaiterRobotTheme
import org.datepollsystems.waiterrobot.shared.features.settings.models.AppTheme
import org.datepollsystems.waiterrobot.shared.root.RootEffect
import org.datepollsystems.waiterrobot.shared.root.RootViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

class MainActivity : AppCompatActivity() {

    private val vm: RootViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        // Switch from SplashScreenTheme to AppTheme
        setTheme(R.style.Theme_WaiterRobot_Main)

        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false) // Support ime padding

        setContent {

            val navEngine = rememberNavHostEngine()
            val navController = navEngine.rememberNavController()
            val scaffoldState = rememberScaffoldState()

            val state = vm.collectAsState().value
            vm.collectSideEffect { handleSideEffects(it, navController, scaffoldState) }

            val useDarkTheme = when (state.selectedTheme) {
                AppTheme.SYSTEM -> isSystemInDarkTheme()
                AppTheme.LIGHT -> false
                AppTheme.DARK -> true
            }

            LaunchedEffect(state.selectedTheme) {
                when (state.selectedTheme) {
                    AppTheme.SYSTEM -> setDefaultNightMode(MODE_NIGHT_FOLLOW_SYSTEM)
                    AppTheme.LIGHT -> setDefaultNightMode(MODE_NIGHT_NO)
                    AppTheme.DARK -> setDefaultNightMode(MODE_NIGHT_YES)
                }
                delegate.applyDayNight()
            }

            WaiterRobotTheme(useDarkTheme) {
                DestinationsNavHost(
                    navGraph = NavGraphs.root,
                    engine = navEngine,
                    navController = navController,
                    dependenciesContainerBuilder = {
                        // Provide the viewModel also to the RootScreen
                        dependency(RootScreenDestination) {
                            dependency(vm)
                        }

                        // Provide the scaffoldState to all screens so that we can show the snackBar
                        dependency(scaffoldState)
                    }
                )
            }
        }

        // Only handle the deep-links when the app is created the first time (but not when it gets recreated)
        if (savedInstanceState == null) {
            intent?.data?.let {
                Logger.d("Started with intent: $it") // TODO inject logger
                vm.onDeepLink(it.toString())
            }
        }
    }

    private suspend fun handleSideEffects(
        effect: RootEffect,
        navigator: NavController,
        scaffoldState: ScaffoldState
    ) {
        when (effect) {
            is RootEffect.Navigate -> navigator.handleNavAction(effect.action)
            is RootEffect.ShowSnackBar -> scaffoldState.snackbarHostState.showSnackbar(effect.message)
        }
    }
}
