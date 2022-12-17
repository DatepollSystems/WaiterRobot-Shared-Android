package org.datepollsystems.waiterrobot.shared.root

import org.datepollsystems.waiterrobot.shared.core.CommonApp
import org.datepollsystems.waiterrobot.shared.core.api.ApiException
import org.datepollsystems.waiterrobot.shared.core.navigation.NavAction
import org.datepollsystems.waiterrobot.shared.core.navigation.Screen
import org.datepollsystems.waiterrobot.shared.core.viewmodel.AbstractViewModel
import org.datepollsystems.waiterrobot.shared.features.auth.repository.AuthRepository
import org.datepollsystems.waiterrobot.shared.generated.localization.*
import org.datepollsystems.waiterrobot.shared.utils.DeepLink
import org.orbitmvi.orbit.syntax.simple.SimpleSyntax
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce

class RootViewModel internal constructor(
    private val authRepo: AuthRepository
) : AbstractViewModel<RootState, RootEffect>(RootState()) {

    override fun onCreate(state: RootState) {
        watchLoginState()
    }

    fun onDeepLink(url: String) = intent {
        logger.d { "Got deeplink: $url" }

        try {
            when (val deepLink = DeepLink.createFromUrl(url)) {
                is DeepLink.Auth -> onAuthDeeplink(deepLink)
            }
        } catch (e: IllegalArgumentException) {
            logger.e(e) { "Could not construct deeplink from url: $url" }
            postSideEffect(RootEffect.ShowSnackBar(L.deepLink.invalid()))
        }
    }

    private suspend fun SimpleSyntax<RootState, RootEffect>.onAuthDeeplink(deepLink: DeepLink.Auth) {
        if (CommonApp.isLoggedIn) {
            postSideEffect(RootEffect.ShowSnackBar(L.deepLink.alreadyLoggedIn()))
            return
        }

        try {
            when (deepLink) {
                is DeepLink.Auth.LoginLink -> authRepo.loginWithToken(deepLink.token)
                is DeepLink.Auth.RegisterLink -> {
                    postSideEffect(
                        RootEffect.Navigate(NavAction.Push(Screen.RegisterScreen(deepLink.token)))
                    )
                }
            }
        } catch (e: ApiException.CredentialsIncorrect) {
            reduceError(L.root.invalidLoginLink.title(), L.root.invalidLoginLink.desc())
        }
    }

    private fun watchLoginState() = intent {
        CommonApp.isLoggedInFlow.collect { loggedIn ->
            reduce { state.copy(isLoggedIn = loggedIn) }
            if (!loggedIn) {
                postSideEffect(RootEffect.Navigate(NavAction.popUpToRoot))
            }
        }
    }
}
