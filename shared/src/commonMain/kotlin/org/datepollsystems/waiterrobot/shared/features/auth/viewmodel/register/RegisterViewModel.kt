package org.datepollsystems.waiterrobot.shared.features.auth.viewmodel.register

import org.datepollsystems.waiterrobot.shared.core.api.ApiException
import org.datepollsystems.waiterrobot.shared.core.navigation.NavAction
import org.datepollsystems.waiterrobot.shared.core.viewmodel.AbstractViewModel
import org.datepollsystems.waiterrobot.shared.features.auth.repository.AuthRepository
import org.datepollsystems.waiterrobot.shared.generated.localization.L
import org.datepollsystems.waiterrobot.shared.generated.localization.desc
import org.datepollsystems.waiterrobot.shared.generated.localization.title
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect

class RegisterViewModel internal constructor(
    private val authRepository: AuthRepository
) : AbstractViewModel<RegisterState, RegisterEffect>(RegisterState()) {

    fun onRegister(name: String, createToken: String) = intent {
        try {
            // TODO check name
            authRepository.createWithToken(createToken, name)
        } catch (e: ApiException.CredentialsIncorrect) {
            reduceError(L.login.invalidCode.title(), L.login.invalidCode.desc())
        }
    }

    fun cancel() = intent {
        // TODO confirm?
        postSideEffect(RegisterEffect.Navigate(NavAction.popUpToRoot))
    }
}
