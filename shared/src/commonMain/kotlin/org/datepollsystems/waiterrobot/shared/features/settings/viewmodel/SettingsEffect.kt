package org.datepollsystems.waiterrobot.shared.features.settings.viewmodel

import org.datepollsystems.waiterrobot.shared.core.navigation.NavAction
import org.datepollsystems.waiterrobot.shared.core.navigation.NavigationEffect
import org.datepollsystems.waiterrobot.shared.core.viewmodel.ViewModelEffect

sealed class SettingsEffect : ViewModelEffect {
    data class Navigate(override val action: NavAction) : SettingsEffect(), NavigationEffect
}
