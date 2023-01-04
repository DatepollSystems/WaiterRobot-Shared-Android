package org.datepollsystems.waiterrobot.shared.features.table.viewmodel.detail

import org.datepollsystems.waiterrobot.shared.core.navigation.NavAction
import org.datepollsystems.waiterrobot.shared.core.navigation.NavigationEffect
import org.datepollsystems.waiterrobot.shared.core.viewmodel.ViewModelEffect

sealed class TableDetailEffect : ViewModelEffect {
    data class Navigate(override val action: NavAction) : TableDetailEffect(), NavigationEffect
}
