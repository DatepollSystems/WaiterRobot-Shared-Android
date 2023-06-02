package org.datepollsystems.waiterrobot.android.ui.core

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import co.touchlab.kermit.Logger
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.navigation.popBackStack
import com.ramcosta.composedestinations.navigation.popUpTo
import com.ramcosta.composedestinations.spec.Direction
import com.ramcosta.composedestinations.spec.Route
import org.datepollsystems.waiterrobot.android.generated.navigation.destinations.*
import org.datepollsystems.waiterrobot.shared.core.navigation.NavAction
import org.datepollsystems.waiterrobot.shared.core.navigation.NavOrViewModelEffect
import org.datepollsystems.waiterrobot.shared.core.navigation.Screen
import org.datepollsystems.waiterrobot.shared.core.viewmodel.AbstractViewModel
import org.datepollsystems.waiterrobot.shared.core.viewmodel.ViewModelEffect
import org.datepollsystems.waiterrobot.shared.core.viewmodel.ViewModelState
import org.koin.androidx.compose.get
import org.koin.core.parameter.parametersOf
import org.orbitmvi.orbit.compose.collectSideEffect

@SuppressLint("ComposableNaming")
@Composable
fun <S : ViewModelState, E : ViewModelEffect> AbstractViewModel<S, E>.handleSideEffects(
    navigator: NavController,
    handler: (suspend (E) -> Unit)? = null
) {
    val logger: Logger = get { parametersOf("handleSideEffects") }
    collectSideEffect { navOrSideEffect ->
        when (navOrSideEffect) {
            is NavOrViewModelEffect.NavEffect -> navigator.handleNavAction(navOrSideEffect.action)
            is NavOrViewModelEffect.VMEffect -> {
                handler?.invoke(navOrSideEffect.effect)
                    ?: logger.w("Side effect ${navOrSideEffect.effect} was not handled.")
            }
        }
    }
}

private fun NavController.handleNavAction(navAction: NavAction) {
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

private val Screen.direction
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

private val Screen.route
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
