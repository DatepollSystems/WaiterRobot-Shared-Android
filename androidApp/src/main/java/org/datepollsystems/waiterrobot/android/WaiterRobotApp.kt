package org.datepollsystems.waiterrobot.android

import android.app.Application
import android.os.Build
import org.datepollsystems.waiterrobot.shared.core.CommonApp
import org.datepollsystems.waiterrobot.shared.core.OS
import org.datepollsystems.waiterrobot.shared.core.di.initKoin
import org.datepollsystems.waiterrobot.shared.generated.localization.localizationContext
import org.koin.android.ext.koin.androidContext

class WaiterRobotApp : Application() {
    override fun onCreate() {
        super.onCreate()

        localizationContext = this

        val phoneModel = Build.MANUFACTURER.replaceFirstChar { it.uppercaseChar() } + " " +
            Build.MODEL.replaceFirstChar { it.uppercaseChar() }

        // Init CommonApp right at the start as e.g. koin might depend on some properties of it
        CommonApp.init(
            os = OS.Android(Build.VERSION.RELEASE),
            appVersion = BuildConfig.VERSION_NAME.substringBeforeLast("-"), // Remove appBuild from version
            appBuild = BuildConfig.VERSION_CODE,
            phoneModel = phoneModel,
            apiBaseUrl = BuildConfig.API_BASE
        )

        initKoin {
            androidContext(this@WaiterRobotApp)
        }
    }
}
