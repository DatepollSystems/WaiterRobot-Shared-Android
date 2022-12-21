package org.datepollsystems.waiterrobot.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.navigation.NavController
import co.touchlab.kermit.Logger
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.dependency
import com.ramcosta.composedestinations.rememberNavHostEngine
import org.datepollsystems.waiterrobot.android.generated.navigation.NavGraphs
import org.datepollsystems.waiterrobot.android.generated.navigation.destinations.RootScreenDestination
import org.datepollsystems.waiterrobot.android.ui.core.handleNavAction
import org.datepollsystems.waiterrobot.android.ui.core.theme.WaiterRobotTheme
import org.datepollsystems.waiterrobot.shared.root.RootEffect
import org.datepollsystems.waiterrobot.shared.root.RootViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.orbitmvi.orbit.compose.collectSideEffect

class MainActivity : ComponentActivity() {

    private val vm: RootViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        // Switch from SplashScreenTheme to AppTheme
        setTheme(R.style.AppTheme)

        super.onCreate(savedInstanceState)

        setContent {

            val navEngine = rememberNavHostEngine()
            val navController = navEngine.rememberNavController()
            val scaffoldState = rememberScaffoldState()

            vm.collectSideEffect { handleSideEffects(it, navController, scaffoldState) }

            WaiterRobotTheme {
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
