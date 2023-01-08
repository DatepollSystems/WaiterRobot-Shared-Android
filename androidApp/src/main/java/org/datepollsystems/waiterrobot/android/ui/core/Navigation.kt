package org.datepollsystems.waiterrobot.android.ui.core

import androidx.navigation.NavController
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.navigation.popBackStack
import com.ramcosta.composedestinations.navigation.popUpTo
import com.ramcosta.composedestinations.spec.Direction
import com.ramcosta.composedestinations.spec.Route
import org.datepollsystems.waiterrobot.android.generated.navigation.destinations.*
import org.datepollsystems.waiterrobot.shared.core.navigation.NavAction
import org.datepollsystems.waiterrobot.shared.core.navigation.Screen

val Screen.direction
    get(): Direction = when (this) {
        Screen.RootScreen -> RootScreenDestination
        Screen.LoginScannerScreen -> LoginScannerScreenDestination
        Screen.SwitchEventScreen -> SwitchEventScreenDestination
        Screen.SettingsScreen -> SettingsScreenDestination
        is Screen.RegisterScreen -> RegisterScreenDestination(this.createToken)
        is Screen.TableDetailScreen -> TableDetailScreenDestination(this.table)
        is Screen.OrderScreen -> OrderScreenDestination(this.table, this.initialItemId)
        is Screen.BillingScreen -> BillingScreenDestination(this.table)
    }

val Screen.route
    get(): Route = when (this) {
        Screen.RootScreen -> RootScreenDestination
        Screen.LoginScannerScreen -> LoginScannerScreenDestination
        Screen.SwitchEventScreen -> SwitchEventScreenDestination
        Screen.SettingsScreen -> SettingsScreenDestination
        is Screen.RegisterScreen -> RegisterScreenDestination
        is Screen.TableDetailScreen -> TableDetailScreenDestination
        is Screen.OrderScreen -> OrderScreenDestination
        is Screen.BillingScreen -> BillingScreenDestination
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
