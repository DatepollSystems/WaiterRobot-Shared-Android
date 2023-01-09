package org.datepollsystems.waiterrobot.shared.core

import org.datepollsystems.waiterrobot.shared.utils.extensions.truncate

class AppInfo(
    val appVersion: String,
    val appBuild: Int,
    phoneModel: String,
    val os: OS,
    apiBaseUrl: String
) {
    val apiBaseUrl = apiBaseUrl.removeSuffix("/") + "/"

    val sessionName =
        "WaiterRobot-${appVersion} ($appBuild); ${phoneModel}; $os".truncate(60)
}

sealed class OS {
    abstract val version: String

    class Android(override val version: String) : OS()
    class Ios(override val version: String) : OS()

    final override fun toString(): String = when (this) {
        is Android -> "Android-${this.version}"
        is Ios -> "iOS-${this.version}"
    }
}
