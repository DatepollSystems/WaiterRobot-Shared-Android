package org.datepollsystems.waiterrobot.shared.features.auth.viewmodel.scanner

import org.datepollsystems.waiterrobot.shared.core.navigation.NavAction
import org.datepollsystems.waiterrobot.shared.core.navigation.Screen
import org.datepollsystems.waiterrobot.shared.core.viewmodel.AbstractViewModel
import org.datepollsystems.waiterrobot.shared.core.viewmodel.ViewState
import org.datepollsystems.waiterrobot.shared.features.auth.repository.AuthRepository
import org.datepollsystems.waiterrobot.shared.utils.DeepLink
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce

class LoginScannerViewModel internal constructor(
    private val authRepository: AuthRepository
) : AbstractViewModel<LoginScannerState, LoginScannerEffect>(LoginScannerState()) {

    fun onCode(code: String) = intent {
        try {
            when (val deepLink = DeepLink.createFromUrl(code)) {
                is DeepLink.Auth.LoginLink -> {
                    reduce { state.withViewState(ViewState.Loading) }
                    authRepository.loginWithToken(deepLink.token)
                    postSideEffect(LoginScannerEffect.Navigate(NavAction.popUpToRoot))
                    reduce { state.withViewState(ViewState.Idle) }
                }
                is DeepLink.Auth.RegisterLink -> {
                    postSideEffect(
                        LoginScannerEffect.Navigate(NavAction.Push(Screen.RegisterScreen(deepLink.token)))
                    )
                }
            }
        } catch (e: Exception) {
            logger.d { "Error with scanned login code: $code" }
            reduceError("Invalid code", "Scanned invalid code")
        }
    }

    fun goBack() = intent {
        postSideEffect(LoginScannerEffect.Navigate(NavAction.Pop))
    }
}
