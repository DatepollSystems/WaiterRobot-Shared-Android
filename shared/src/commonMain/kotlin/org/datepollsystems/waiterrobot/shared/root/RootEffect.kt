package org.datepollsystems.waiterrobot.shared.root

import org.datepollsystems.waiterrobot.shared.core.navigation.NavAction
import org.datepollsystems.waiterrobot.shared.core.navigation.NavigationEffect
import org.datepollsystems.waiterrobot.shared.core.viewmodel.ViewModelEffect

sealed class RootEffect : ViewModelEffect {
    data class Navigate(override val action: NavAction) : RootEffect(), NavigationEffect
    data class ShowSnackBar(val message: String) : RootEffect()
}
