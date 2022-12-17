package org.datepollsystems.waiterrobot.shared.features.auth.viewmodel

import org.datepollsystems.waiterrobot.shared.core.navigation.NavAction
import org.datepollsystems.waiterrobot.shared.core.navigation.Screen
import org.datepollsystems.waiterrobot.shared.core.viewmodel.AbstractViewModel
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect

class LoginViewModel internal constructor() :
    AbstractViewModel<LoginState, LoginEffect>(LoginState()) {

    fun openScanner() = intent {
        postSideEffect(LoginEffect.Navigate(NavAction.Push(Screen.LoginScannerScreen)))
    }
}
