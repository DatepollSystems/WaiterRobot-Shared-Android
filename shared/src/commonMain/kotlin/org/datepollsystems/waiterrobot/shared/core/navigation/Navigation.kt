package org.datepollsystems.waiterrobot.shared.core.navigation

sealed class Screen {
    object RootScreen : Screen()
}

sealed class NavAction {
    object Pop : NavAction()
    data class Push(val screen: Screen) : NavAction()
    data class PopUpTo(val screen: Screen, val inclusive: Boolean) : NavAction()
    data class PopUpAndPush(val screen: Screen, val popUpTo: Screen, val inclusive: Boolean) :
        NavAction()

    companion object {
        val popUpToRoot get() = PopUpTo(Screen.RootScreen, inclusive = false)
    }
}

interface NavigationEffect {
    val action: NavAction
}
