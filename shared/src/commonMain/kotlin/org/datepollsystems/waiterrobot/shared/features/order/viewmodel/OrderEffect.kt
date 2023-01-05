package org.datepollsystems.waiterrobot.shared.features.order.viewmodel

import org.datepollsystems.waiterrobot.shared.core.navigation.NavAction
import org.datepollsystems.waiterrobot.shared.core.navigation.NavigationEffect
import org.datepollsystems.waiterrobot.shared.core.viewmodel.ViewModelEffect

sealed class OrderEffect : ViewModelEffect {
    data class Navigate(override val action: NavAction) : OrderEffect(), NavigationEffect
}
