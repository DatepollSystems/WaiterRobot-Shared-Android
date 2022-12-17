package org.datepollsystems.waiterrobot.shared.features.auth.viewmodel.scanner

import org.datepollsystems.waiterrobot.shared.core.navigation.NavAction
import org.datepollsystems.waiterrobot.shared.core.navigation.NavigationEffect
import org.datepollsystems.waiterrobot.shared.core.viewmodel.ViewModelEffect

sealed class LoginScannerEffect : ViewModelEffect {
    data class Navigate(override val action: NavAction) : LoginScannerEffect(), NavigationEffect
}
