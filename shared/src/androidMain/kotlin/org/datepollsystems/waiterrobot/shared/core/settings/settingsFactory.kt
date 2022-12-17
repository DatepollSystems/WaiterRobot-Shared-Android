package org.datepollsystems.waiterrobot.shared.core.settings

import android.content.Context
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.SharedPreferencesSettings

lateinit var context: Context

internal actual val settingsFactory: SettingsFactory by lazy {
    object : SettingsFactory {
        override fun create(name: String?): ObservableSettings {
            return SharedPreferencesSettings.Factory(context).create(name)
        }
    }
}
