package org.datepollsystems.waiterrobot.shared.features.auth.viewmodel.register

import org.datepollsystems.waiterrobot.shared.core.api.ApiException
import org.datepollsystems.waiterrobot.shared.core.viewmodel.AbstractViewModel
import org.datepollsystems.waiterrobot.shared.core.viewmodel.ViewState
import org.datepollsystems.waiterrobot.shared.features.auth.repository.AuthRepository
import org.datepollsystems.waiterrobot.shared.generated.localization.L
import org.datepollsystems.waiterrobot.shared.generated.localization.desc
import org.datepollsystems.waiterrobot.shared.generated.localization.title
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce

class RegisterViewModel internal constructor(
    private val authRepository: AuthRepository
) : AbstractViewModel<RegisterState, RegisterEffect>(RegisterState()) {

    fun onRegister(name: String, createToken: String) = intent {
        reduce { state.withViewState(ViewState.Loading) }
        try {
            // TODO check name
            authRepository.createWithToken(createToken, name)
            navigator.popUpToRoot()
            reduce { state.withViewState(ViewState.Idle) }
        } catch (e: ApiException.CredentialsIncorrect) {
            reduceError(L.login.invalidCode.title(), L.login.invalidCode.desc())
        }
    }

    fun cancel() = intent {
        // TODO confirm?
        navigator.popUpToRoot()
    }
}
