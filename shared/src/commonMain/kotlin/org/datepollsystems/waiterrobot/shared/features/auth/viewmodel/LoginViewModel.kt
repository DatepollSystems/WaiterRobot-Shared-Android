package org.datepollsystems.waiterrobot.shared.features.auth.viewmodel

import org.datepollsystems.waiterrobot.shared.core.navigation.Screen
import org.datepollsystems.waiterrobot.shared.core.viewmodel.AbstractViewModel
import org.orbitmvi.orbit.syntax.simple.intent

class LoginViewModel internal constructor() :
    AbstractViewModel<LoginState, LoginEffect>(LoginState()) {

    fun openScanner() = intent {
        navigator.push(Screen.LoginScannerScreen)
    }
}
