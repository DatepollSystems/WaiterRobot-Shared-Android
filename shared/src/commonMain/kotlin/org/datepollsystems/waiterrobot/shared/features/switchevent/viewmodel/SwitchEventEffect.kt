package org.datepollsystems.waiterrobot.shared.features.switchevent.viewmodel

import org.datepollsystems.waiterrobot.shared.core.navigation.NavAction
import org.datepollsystems.waiterrobot.shared.core.navigation.NavigationEffect
import org.datepollsystems.waiterrobot.shared.core.viewmodel.ViewModelEffect

sealed class SwitchEventEffect : ViewModelEffect {
    data class Navigation(override val action: NavAction) : SwitchEventEffect(), NavigationEffect
}
