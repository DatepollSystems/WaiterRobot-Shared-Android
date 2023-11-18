package org.datepollsystems.waiterrobot.shared.features.auth.viewmodel.scanner

import kotlinx.coroutines.CancellationException
import org.datepollsystems.waiterrobot.shared.core.navigation.Screen
import org.datepollsystems.waiterrobot.shared.core.viewmodel.AbstractViewModel
import org.datepollsystems.waiterrobot.shared.core.viewmodel.ViewState
import org.datepollsystems.waiterrobot.shared.features.auth.repository.AuthRepository
import org.datepollsystems.waiterrobot.shared.generated.localization.L
import org.datepollsystems.waiterrobot.shared.generated.localization.desc
import org.datepollsystems.waiterrobot.shared.generated.localization.title
import org.datepollsystems.waiterrobot.shared.utils.DeepLink
import org.orbitmvi.orbit.syntax.simple.intent
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
                    navigator.popUpToRoot()
                    reduce { state.withViewState(ViewState.Idle) }
                }
                is DeepLink.Auth.RegisterLink -> {
                    navigator.push(Screen.RegisterScreen(deepLink.token))
                }
            }
        } catch (e: CancellationException) {
            throw e
        } catch (_: Exception) {
            logger.d { "Error with scanned login code: $code" }
            reduceError(L.login.invalidCode.title(), L.login.invalidCode.desc())
        }
    }

    fun goBack() = intent {
        navigator.pop()
    }
}
