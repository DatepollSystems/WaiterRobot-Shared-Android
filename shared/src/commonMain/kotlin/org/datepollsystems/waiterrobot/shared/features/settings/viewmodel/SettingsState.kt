package org.datepollsystems.waiterrobot.shared.features.settings.viewmodel

import org.datepollsystems.waiterrobot.shared.core.CommonApp
import org.datepollsystems.waiterrobot.shared.core.viewmodel.ViewModelState
import org.datepollsystems.waiterrobot.shared.core.viewmodel.ViewState
import org.datepollsystems.waiterrobot.shared.features.settings.models.AppTheme
import org.datepollsystems.waiterrobot.shared.generated.localization.L
import org.datepollsystems.waiterrobot.shared.generated.localization.desc

data class SettingsState(
    val currentAppTheme: AppTheme = CommonApp.settings.theme,
    val skipMoneyBackDialog: Boolean = CommonApp.settings.skipMoneyBackDialog,
    override val viewState: ViewState = ViewState.Idle
) : ViewModelState() {
    val versionString
        get() = L.settings.about.version.desc(
            CommonApp.appInfo.appVersion,
            CommonApp.appInfo.appBuild.toString()
        )

    override fun withViewState(viewState: ViewState): SettingsState = copy(viewState = viewState)
}
