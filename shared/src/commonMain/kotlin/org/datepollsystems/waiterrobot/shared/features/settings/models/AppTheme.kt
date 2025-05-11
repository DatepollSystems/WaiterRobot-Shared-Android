package org.datepollsystems.waiterrobot.shared.features.settings.models

import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.resources.desc.desc
import kotlinx.serialization.Serializable
import org.datepollsystems.waiterrobot.shared.localization.MR

@Serializable
enum class AppTheme {
    SYSTEM,
    LIGHT,
    DARK;

    fun settingsText(): StringDesc = when (this) {
        SYSTEM -> MR.strings.settings_general_darkMode_useSystem.desc()
        LIGHT -> MR.strings.dialog_off.desc()
        DARK -> MR.strings.dialog_on.desc()
    }

    companion object {
        @Suppress("unused") // Only iOS
        fun valueList() = entries
    }
}
