package org.datepollsystems.waiterrobot.shared.core.settings

import com.russhwolf.settings.NSUserDefaultsSettings
import com.russhwolf.settings.ObservableSettings

internal actual val settingsFactory: SettingsFactory by lazy {
    object : SettingsFactory {
        override fun create(name: String?): ObservableSettings {
            return NSUserDefaultsSettings.Factory().create(name)
        }
    }
}
