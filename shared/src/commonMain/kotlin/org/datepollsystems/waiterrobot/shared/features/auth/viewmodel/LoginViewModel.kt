package org.datepollsystems.waiterrobot.shared.features.auth.viewmodel

import kotlinx.coroutines.CancellationException
import org.datepollsystems.waiterrobot.shared.core.CommonApp
import org.datepollsystems.waiterrobot.shared.core.navigation.Screen
import org.datepollsystems.waiterrobot.shared.core.viewmodel.AbstractViewModel
import org.datepollsystems.waiterrobot.shared.core.viewmodel.ViewState
import org.datepollsystems.waiterrobot.shared.features.auth.repository.AuthRepository
import org.datepollsystems.waiterrobot.shared.utils.DeepLink
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce

class LoginViewModel internal constructor(
    private val authRepository: AuthRepository
) : AbstractViewModel<LoginState, LoginEffect>(LoginState()) {

    fun openScanner() = intent {
        navigator.push(Screen.LoginScannerScreen)
    }

    fun onDebugLogin(link: String) = intent {
        @Suppress("TooGenericExceptionCaught")
        try {
            when (val deepLink = DeepLink.createFromUrl(link)) {
                is DeepLink.Auth.LoginLink -> {
                    reduce { state.withViewState(ViewState.Loading) }
                    authRepository.loginWaiter(deepLink)
                    navigator.replaceRoot(CommonApp.getNextRootScreen())
                    reduce { state.withViewState(ViewState.Idle) }
                }

                is DeepLink.Auth.RegisterLink -> {
                    navigator.push(Screen.RegisterScreen(deepLink))
                }
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            logger.d(e) { "Error with debug login link: $link" }
        }
    }
}
