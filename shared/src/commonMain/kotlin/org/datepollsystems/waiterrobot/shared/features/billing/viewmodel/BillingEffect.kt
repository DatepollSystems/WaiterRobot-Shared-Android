package org.datepollsystems.waiterrobot.shared.features.billing.viewmodel

import org.datepollsystems.waiterrobot.shared.core.navigation.NavAction
import org.datepollsystems.waiterrobot.shared.core.navigation.NavigationEffect
import org.datepollsystems.waiterrobot.shared.core.viewmodel.ViewModelEffect

sealed class BillingEffect : ViewModelEffect {
    data class Navigate(override val action: NavAction) : BillingEffect(), NavigationEffect
}
