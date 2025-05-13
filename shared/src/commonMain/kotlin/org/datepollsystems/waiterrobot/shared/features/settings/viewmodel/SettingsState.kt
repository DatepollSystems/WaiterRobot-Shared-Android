package org.datepollsystems.waiterrobot.shared.features.settings.viewmodel

import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.resources.format
import org.datepollsystems.waiterrobot.shared.core.CommonApp
import org.datepollsystems.waiterrobot.shared.core.viewmodel.ViewModelState
import org.datepollsystems.waiterrobot.shared.features.settings.models.AppTheme
import org.datepollsystems.waiterrobot.shared.localization.MR

data class SettingsState(
    val currentAppTheme: AppTheme = CommonApp.settings.theme,
    val skipMoneyBackDialog: Boolean = CommonApp.settings.skipMoneyBackDialog,
    val paymentSelectAllProductsByDefault: Boolean = CommonApp.settings.paymentSelectAllProductsByDefault,
) : ViewModelState {
    val versionString: StringDesc
        get() = MR.strings.settings_about_version_desc.format(
            CommonApp.appInfo.appVersion,
            CommonApp.appInfo.appBuild
        )
}
