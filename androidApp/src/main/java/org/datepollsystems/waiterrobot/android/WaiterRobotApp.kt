package org.datepollsystems.waiterrobot.android

import android.app.Application
import org.datepollsystems.waiterrobot.shared.core.di.initKoin
import org.datepollsystems.waiterrobot.shared.generated.localization.localizationContext

class WaiterRobotApp : Application() {
    override fun onCreate() {
        super.onCreate()

        initKoin()

        localizationContext = this
    }
}
