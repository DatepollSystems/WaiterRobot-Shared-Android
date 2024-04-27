package org.datepollsystems.waiterrobot.shared.features.auth.viewmodel.register

import org.datepollsystems.waiterrobot.shared.core.CommonApp
import org.datepollsystems.waiterrobot.shared.core.data.api.ApiException
import org.datepollsystems.waiterrobot.shared.core.navigation.Screen
import org.datepollsystems.waiterrobot.shared.core.viewmodel.AbstractViewModel
import org.datepollsystems.waiterrobot.shared.core.viewmodel.ViewState
import org.datepollsystems.waiterrobot.shared.features.auth.repository.AuthRepository
import org.datepollsystems.waiterrobot.shared.features.switchevent.models.Event
import org.datepollsystems.waiterrobot.shared.generated.localization.L
import org.datepollsystems.waiterrobot.shared.generated.localization.desc
import org.datepollsystems.waiterrobot.shared.generated.localization.title
import org.datepollsystems.waiterrobot.shared.utils.DeepLink
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce

class RegisterViewModel internal constructor(
    private val authRepository: AuthRepository
) : AbstractViewModel<RegisterState, RegisterEffect>(RegisterState()) {

    fun onRegister(name: String, registerLink: DeepLink.Auth.RegisterLink) = intent {
        reduce { state.withViewState(ViewState.Loading) }
        try {
            // TODO check name
            authRepository.createWaiter(registerLink, name)
            if (CommonApp.settings.selectedEvent?.stripeSettings is Event.StripeSettings.Enabled) {
                navigator.push(Screen.StripeInitializationScreen)
            } else {
                navigator.popUpToRoot()
            }
            reduce { state.withViewState(ViewState.Idle) }
        } catch (_: ApiException.CredentialsIncorrect) {
            reduceError(L.login.invalidCode.title(), L.login.invalidCode.desc())
        }
    }

    fun cancel() = intent {
        // TODO confirm?
        navigator.popUpToRoot()
    }
}
