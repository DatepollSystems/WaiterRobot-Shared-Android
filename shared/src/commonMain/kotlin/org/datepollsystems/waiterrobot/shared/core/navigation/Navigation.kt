package org.datepollsystems.waiterrobot.shared.core.navigation

import org.datepollsystems.waiterrobot.shared.core.viewmodel.ViewModelEffect
import org.datepollsystems.waiterrobot.shared.features.table.models.Table

sealed class Screen {
    object RootScreen : Screen()
    object LoginScannerScreen : Screen()
    object SwitchEventScreen : Screen()
    object SettingsScreen : Screen()

    data class RegisterScreen(val createToken: String) : Screen()
    data class TableDetailScreen(val table: Table) : Screen()
    data class OrderScreen(val table: Table, val initialItemId: Long?) : Screen()
    data class BillingScreen(val table: Table) : Screen()
}

sealed class NavAction {
    object Pop : NavAction()
    data class Push(val screen: Screen) : NavAction()
    data class PopUpTo(val screen: Screen, val inclusive: Boolean) : NavAction()
    data class PopUpAndPush(val screen: Screen, val popUpTo: Screen, val inclusive: Boolean) :
        NavAction()
}

sealed class NavOrViewModelEffect<out E : ViewModelEffect> {
    class NavEffect<E : ViewModelEffect>(val action: NavAction) : NavOrViewModelEffect<E>()
    class VMEffect<E : ViewModelEffect>(val effect: E) : NavOrViewModelEffect<E>()
}
