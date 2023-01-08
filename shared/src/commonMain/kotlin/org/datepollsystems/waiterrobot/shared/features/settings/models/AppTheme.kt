package org.datepollsystems.waiterrobot.shared.features.settings.models

import org.datepollsystems.waiterrobot.shared.generated.localization.L
import org.datepollsystems.waiterrobot.shared.generated.localization.off
import org.datepollsystems.waiterrobot.shared.generated.localization.on
import org.datepollsystems.waiterrobot.shared.generated.localization.useSystem

enum class AppTheme {
    SYSTEM,
    LIGHT,
    DARK;

    fun settingsText() = when (this) {
        SYSTEM -> L.settings.darkMode.useSystem()
        LIGHT -> L.dialog.off()
        DARK -> L.dialog.on()
    }

    companion object {
        fun fromSettings(name: String?) =
            name?.runCatching { valueOf(name) }?.getOrNull() ?: SYSTEM

        @Suppress("unused") // Only iOS
        fun valueList() = values().toList()
    }
}
