package org.datepollsystems.waiterrobot.android.ui.core

import androidx.navigation.NavController
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.navigation.popBackStack
import com.ramcosta.composedestinations.navigation.popUpTo
import com.ramcosta.composedestinations.spec.Direction
import com.ramcosta.composedestinations.spec.Route
import org.datepollsystems.waiterrobot.android.generated.navigation.destinations.LoginScannerScreenDestination
import org.datepollsystems.waiterrobot.android.generated.navigation.destinations.RegisterScreenDestination
import org.datepollsystems.waiterrobot.android.generated.navigation.destinations.RootScreenDestination
import org.datepollsystems.waiterrobot.android.generated.navigation.destinations.SwitchEventScreenDestination
import org.datepollsystems.waiterrobot.shared.core.navigation.NavAction
import org.datepollsystems.waiterrobot.shared.core.navigation.Screen

val Screen.direction
    get(): Direction = when (this) {
        Screen.RootScreen -> RootScreenDestination
        Screen.LoginScannerScreen -> LoginScannerScreenDestination
        Screen.SwitchEventScreen -> SwitchEventScreenDestination
        is Screen.RegisterScreen -> RegisterScreenDestination(this.createToken)
    }

val Screen.route
    get(): Route = when (this) {
        Screen.RootScreen -> RootScreenDestination
        Screen.LoginScannerScreen -> LoginScannerScreenDestination
        Screen.SwitchEventScreen -> SwitchEventScreenDestination
        is Screen.RegisterScreen -> RegisterScreenDestination
    }

fun NavController.handleNavAction(navAction: NavAction) {
    when (navAction) {
        NavAction.Pop -> popBackStack()
        is NavAction.PopUpTo -> popBackStack(navAction.screen.route, navAction.inclusive)
        is NavAction.Push -> navigate(navAction.screen.direction)
        is NavAction.PopUpAndPush -> {
            navigate(navAction.screen.direction) {
                popUpTo(navAction.popUpTo.route) {
                    inclusive = navAction.inclusive
                }
            }
        }
    }
}
