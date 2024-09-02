package org.datepollsystems.waiterrobot.shared.features.settings.viewmodel

import org.datepollsystems.waiterrobot.shared.core.viewmodel.ViewModelEffect

sealed class SettingsEffect : ViewModelEffect {
    data object ConfirmSkipMoneyBackDialog : SettingsEffect()
}
