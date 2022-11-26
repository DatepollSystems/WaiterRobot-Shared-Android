package org.datepollsystems.waiterrobot.android

import android.app.Application
import org.datepollsystems.waiterrobot.shared.core.di.initKoin

class WaiterRobotApp : Application() {
    override fun onCreate() {
        super.onCreate()

        initKoin()
    }
}
