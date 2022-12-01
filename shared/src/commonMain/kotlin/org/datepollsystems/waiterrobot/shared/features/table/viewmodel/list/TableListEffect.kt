package org.datepollsystems.waiterrobot.shared.features.table.viewmodel.list

import org.datepollsystems.waiterrobot.shared.core.navigation.NavAction
import org.datepollsystems.waiterrobot.shared.core.navigation.NavigationEffect
import org.datepollsystems.waiterrobot.shared.core.viewmodel.ViewModelEffect

sealed class TableListEffect : ViewModelEffect {
    data class Navigate(override val action: NavAction) : TableListEffect(), NavigationEffect
}
