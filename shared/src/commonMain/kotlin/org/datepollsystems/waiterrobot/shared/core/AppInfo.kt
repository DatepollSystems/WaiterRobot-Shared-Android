package org.datepollsystems.waiterrobot.shared.core

import org.datepollsystems.waiterrobot.shared.utils.extensions.truncate
import kotlin.properties.Delegates

object AppInfo {
    lateinit var appVersion: String
        private set
    lateinit var phoneModel: String
        private set
    lateinit var os: OS
        private set
    var appBuild by Delegates.notNull<Int>()
        private set

    fun init(appVersion: String, appBuild: Int, phoneModel: String, os: OS) {
        this.appVersion = appVersion
        this.appBuild = appBuild
        this.phoneModel = phoneModel
        this.os = os
    }

    val sessionName: String by lazy {
        "WaiterRobot-${appVersion} ($appBuild); ${phoneModel}; $os".truncate(60)
    }
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
