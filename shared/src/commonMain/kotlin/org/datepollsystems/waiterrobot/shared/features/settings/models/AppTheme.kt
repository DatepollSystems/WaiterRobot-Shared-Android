package org.datepollsystems.waiterrobot.shared.features.settings.models

import kotlinx.serialization.Serializable
import org.datepollsystems.waiterrobot.shared.generated.localization.L
import org.datepollsystems.waiterrobot.shared.generated.localization.off
import org.datepollsystems.waiterrobot.shared.generated.localization.on
import org.datepollsystems.waiterrobot.shared.generated.localization.useSystem

@Serializable
enum class AppTheme {
    SYSTEM,
    LIGHT,
    DARK;

    fun settingsText() = when (this) {
        SYSTEM -> L.settings.general.darkMode.useSystem()
        LIGHT -> L.dialog.off()
        DARK -> L.dialog.on()
    }

    companion object {
        @Suppress("unused") // Only iOS
        fun valueList() = values().toList()
    }
}
