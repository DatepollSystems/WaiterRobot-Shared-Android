package org.datepollsystems.waiterrobot.shared.features.auth.viewmodel.scanner

import dev.icerock.moko.resources.desc.desc
import kotlinx.coroutines.CancellationException
import org.datepollsystems.waiterrobot.shared.core.navigation.Screen
import org.datepollsystems.waiterrobot.shared.core.viewmodel.AbstractViewModel
import org.datepollsystems.waiterrobot.shared.core.viewmodel.DialogState
import org.datepollsystems.waiterrobot.shared.core.viewmodel.ViewState
import org.datepollsystems.waiterrobot.shared.features.auth.repository.AuthRepository
import org.datepollsystems.waiterrobot.shared.localization.MR
import org.datepollsystems.waiterrobot.shared.utils.DeepLink
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce

class LoginScannerViewModel internal constructor(
    private val authRepository: AuthRepository
) : AbstractViewModel<LoginScannerState, LoginScannerEffect>(LoginScannerState()) {

    fun onCode(code: String) = intent {
        @Suppress("TooGenericExceptionCaught")
        try {
            when (val deepLink = DeepLink.createFromUrl(code)) {
                is DeepLink.Auth.LoginLink -> {
                    reduce { state.copy(viewState = ViewState.Loading) }
                    authRepository.loginWaiter(deepLink)
                    reduce { state.copy(viewState = ViewState.Idle) }
                }

                is DeepLink.Auth.RegisterLink -> {
                    navigator.push(Screen.RegisterScreen(deepLink))
                }
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            logger.d(e) { "Error with scanned login code: $code" }
            val dismiss: () -> Unit = {
                intent { reduce { state.copy(viewState = ViewState.Idle) } }
            }
            reduce {
                state.copy(
                    viewState = ViewState.Error(
                        MR.strings.login_scanner_invalidCode_title.desc(),
                        MR.strings.login_scanner_invalidCode_desc.desc(),
                        onDismiss = dismiss,
                        primaryButton = DialogState.Button(MR.strings.dialog_ok.desc(), dismiss)
                    )
                )
            }
        }
    }

    fun goBack() = intent {
        navigator.pop()
    }
}
