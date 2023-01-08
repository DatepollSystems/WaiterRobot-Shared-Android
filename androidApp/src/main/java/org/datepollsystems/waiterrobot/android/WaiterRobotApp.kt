package org.datepollsystems.waiterrobot.android

import android.app.Application
import android.os.Build
import org.datepollsystems.waiterrobot.shared.core.AppInfo
import org.datepollsystems.waiterrobot.shared.core.OS
import org.datepollsystems.waiterrobot.shared.core.di.initKoin
import org.datepollsystems.waiterrobot.shared.core.settings.context
import org.datepollsystems.waiterrobot.shared.generated.localization.localizationContext

class WaiterRobotApp : Application() {
    override fun onCreate() {
        super.onCreate()

        initKoin()

        val phoneModel = Build.MANUFACTURER.replaceFirstChar { it.uppercaseChar() } + " " +
            Build.MODEL.replaceFirstChar { it.uppercaseChar() }

        context = this
        localizationContext = this
        AppInfo.init(
            os = OS.Android(Build.VERSION.RELEASE),
            appVersion = BuildConfig.VERSION_NAME.substringBefore("-"),
            appBuild = BuildConfig.VERSION_CODE,
            phoneModel = phoneModel
        )

        context = this
        localizationContext = this
    }
}
