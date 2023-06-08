package org.datepollsystems.waiterrobot.android

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.core.view.WindowCompat
import co.touchlab.kermit.Logger
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
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
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.orbitmvi.orbit.compose.collectAsState

class MainActivity : AppCompatActivity() {

    private val vm: RootViewModel by viewModel()
    private val appUpdateManager by lazy { AppUpdateManagerFactory.create(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false) // Support ime padding

        // Switch from SplashScreenTheme to AppTheme
        setTheme(R.style.Theme_WaiterRobot_Main)

        checkForUpdate()

        setContent {
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

            LaunchedEffect(state.selectedTheme) {
                when (state.selectedTheme) {
                    AppTheme.SYSTEM -> setDefaultNightMode(MODE_NIGHT_FOLLOW_SYSTEM)
                    AppTheme.LIGHT -> setDefaultNightMode(MODE_NIGHT_NO)
                    AppTheme.DARK -> setDefaultNightMode(MODE_NIGHT_YES)
                }
                delegate.applyDayNight()
            }

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

        // Only handle the deep-links when the app is created the first time (but not when it gets recreated)
        if (savedInstanceState == null) {
            intent?.data?.let {
                Logger.d("Started with intent: $it") // TODO inject logger
                vm.onDeepLink(it.toString())
            }
        }
    }

    override fun onResume() {
        super.onResume()

        appUpdateManager.appUpdateInfo
            .addOnSuccessListener { appUpdateInfo ->
                if (appUpdateInfo.updateAvailability()
                    == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS
                ) {
                    // If an in-app update is already running, resume the update.
                    appUpdateManager.startUpdateFlow(
                        appUpdateInfo,
                        this,
                        AppUpdateOptions.defaultOptions(AppUpdateType.IMMEDIATE),
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

    private fun checkForUpdate() {
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (
                appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && (appUpdateInfo.clientVersionStalenessDays() ?: -1) >= 3
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
            ) {
                appUpdateManager.startUpdateFlow(
                    appUpdateInfo,
                    this,
                    AppUpdateOptions.defaultOptions(AppUpdateType.IMMEDIATE)
                )
            }
        }
    }
}
