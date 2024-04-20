package org.datepollsystems.waiterrobot.android

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import co.touchlab.kermit.Logger
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.until
import org.datepollsystems.waiterrobot.android.stripe.Stripe
import org.datepollsystems.waiterrobot.android.ui.RootView
import org.datepollsystems.waiterrobot.shared.core.CommonApp
import org.datepollsystems.waiterrobot.shared.core.CommonApp.MIN_UPDATE_INFO_HOURS
import org.datepollsystems.waiterrobot.shared.features.settings.models.AppTheme
import org.datepollsystems.waiterrobot.shared.root.RootViewModel
import org.datepollsystems.waiterrobot.shared.utils.extensions.defaultOnNull
import org.koin.android.ext.android.getKoin
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val vm: RootViewModel by viewModel()
    private val appUpdateManager by lazy { AppUpdateManagerFactory.create(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false) // Support ime padding

        // Switch from SplashScreenTheme to AppTheme
        setTheme(R.style.Theme_WaiterRobot_Main)

        setContent {
            RootView(vm, ::onAppThemeChange)
        }

        checkForUpdate()

        // Only handle the deep-links when the app is created the first time (but not when it gets recreated)
        if (savedInstanceState == null) {
            intent?.data?.let {
                Logger.d("Started with intent: $it") // TODO inject logger
                vm.onDeepLink(it.toString())
            }
        }

        // TODO find a better place/context to ask for permission
        //  e.g. when switching/selecting an event where stripe is enabled?
        //    - What to do when is enabled for the event when already logged in to the event?
        // Add a additional screen between event selection and starting which handles the terminal setup?
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            val permissions = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION)
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_LOCATION)
        } else if (CommonApp.settings.selectedEventId != -1L) {
            getKoin().get<CoroutineScope>().launch {
                Stripe.connectLocalReader()
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_CODE_LOCATION && grantResults.isNotEmpty() &&
            grantResults[0] != PackageManager.PERMISSION_GRANTED
        ) {
            @Suppress("TooGenericExceptionThrown")
            throw RuntimeException("Location services are required in order to connect to a reader.")
        }
    }

    private fun checkForUpdate() {
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (
                appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE &&
                appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE) &&
                CommonApp.settings.lastUpdateAvailableNote // Show max once a day
                    .defaultOnNull(Instant.DISTANT_PAST)
                    .until(Clock.System.now(), DateTimeUnit.HOUR) > MIN_UPDATE_INFO_HOURS
            ) {
                CommonApp.settings.lastUpdateAvailableNote = Clock.System.now()
                appUpdateManager.startUpdateFlow(
                    appUpdateInfo,
                    this,
                    AppUpdateOptions.defaultOptions(AppUpdateType.IMMEDIATE)
                )
            }
        }
    }

    private fun onAppThemeChange(theme: AppTheme) {
        when (theme) {
            AppTheme.SYSTEM -> setDefaultNightMode(MODE_NIGHT_FOLLOW_SYSTEM)
            AppTheme.LIGHT -> setDefaultNightMode(MODE_NIGHT_NO)
            AppTheme.DARK -> setDefaultNightMode(MODE_NIGHT_YES)
        }
        delegate.applyDayNight()
    }

    companion object {
        private const val REQUEST_CODE_LOCATION = 9857
    }
}
