package org.datepollsystems.waiterrobot.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavController
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

            vm.collectSideEffect { handleSideEffects(it, navController) }

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
                    }
                )
            }
        }
    }

    private fun handleSideEffects(effect: RootEffect, navigator: NavController) {
        when (effect) {
            is RootEffect.Navigate -> navigator.handleNavAction(effect.action)
        }
    }
}
