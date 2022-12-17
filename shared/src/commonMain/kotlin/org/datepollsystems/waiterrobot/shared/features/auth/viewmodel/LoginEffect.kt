package org.datepollsystems.waiterrobot.shared.features.auth.viewmodel

import org.datepollsystems.waiterrobot.shared.core.navigation.NavAction
import org.datepollsystems.waiterrobot.shared.core.navigation.NavigationEffect
import org.datepollsystems.waiterrobot.shared.core.viewmodel.ViewModelEffect

sealed class LoginEffect : ViewModelEffect {
    data class Navigate(override val action: NavAction) : LoginEffect(), NavigationEffect
}
