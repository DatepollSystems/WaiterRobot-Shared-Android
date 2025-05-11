package org.datepollsystems.waiterrobot.shared.features.auth.viewmodel.register

import dev.icerock.moko.resources.desc.desc
import org.datepollsystems.waiterrobot.shared.core.data.remote.ApiException
import org.datepollsystems.waiterrobot.shared.core.viewmodel.AbstractViewModel
import org.datepollsystems.waiterrobot.shared.core.viewmodel.DialogState
import org.datepollsystems.waiterrobot.shared.core.viewmodel.ViewState
import org.datepollsystems.waiterrobot.shared.features.auth.repository.AuthRepository
import org.datepollsystems.waiterrobot.shared.localization.MR
import org.datepollsystems.waiterrobot.shared.utils.DeepLink
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce

class RegisterViewModel internal constructor(
    private val authRepository: AuthRepository
) : AbstractViewModel<RegisterState, RegisterEffect>(RegisterState()) {

    fun onRegister(name: String, registerLink: DeepLink.Auth.RegisterLink) = intent {
        reduce { state.copy(viewState = ViewState.Loading) }
        try {
            // TODO check name
            authRepository.createWaiter(registerLink, name)
            reduce { state.copy(viewState = ViewState.Idle) }
        } catch (_: ApiException.CredentialsIncorrect) {
            val dismiss: () -> Unit = {
                intent { reduce { state.copy(viewState = ViewState.Idle) } }
            }
            reduce {
                state.copy(
                    viewState = ViewState.Error(
                        MR.strings.login_scanner_invalidCode_title.desc(),
                        MR.strings.root_invalidLoginLink_desc.desc(),
                        onDismiss = dismiss,
                        primaryButton = DialogState.Button(MR.strings.dialog_ok.desc(), dismiss)
                    )
                )
            }
        }
    }

    fun cancel() = intent {
        // TODO confirm?
        navigator.pop()
    }
}
