package org.datepollsystems.waiterrobot.shared.core.settings

import com.russhwolf.settings.ObservableSettings

internal expect val settingsFactory: SettingsFactory

internal interface SettingsFactory {
    fun create(name: String? = null): ObservableSettings
}
