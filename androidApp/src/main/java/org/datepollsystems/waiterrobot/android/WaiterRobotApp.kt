package org.datepollsystems.waiterrobot.android

import android.app.Application
import android.os.Build
import org.datepollsystems.waiterrobot.shared.core.CommonApp
import org.datepollsystems.waiterrobot.shared.core.OS
import org.datepollsystems.waiterrobot.shared.core.di.initKoin
import org.datepollsystems.waiterrobot.shared.core.settings.context
import org.datepollsystems.waiterrobot.shared.generated.localization.localizationContext

class WaiterRobotApp : Application() {
    override fun onCreate() {
        super.onCreate()

        context = this
        localizationContext = this

        val phoneModel = Build.MANUFACTURER.replaceFirstChar { it.uppercaseChar() } + " " +
            Build.MODEL.replaceFirstChar { it.uppercaseChar() }

        // Init CommonApp right at the start as e.g. koin might depend on some properties of it
        CommonApp.init(
            os = OS.Android(Build.VERSION.RELEASE),
            appVersion = BuildConfig.VERSION_NAME.substringBefore("-"),
            appBuild = BuildConfig.VERSION_CODE,
            phoneModel = phoneModel,
            apiBaseUrl = "https://my.kellner.team/"
        )

        initKoin()
    }
}
