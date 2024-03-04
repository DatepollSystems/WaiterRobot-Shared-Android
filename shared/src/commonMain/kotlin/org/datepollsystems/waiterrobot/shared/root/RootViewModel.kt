package org.datepollsystems.waiterrobot.shared.root

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.datepollsystems.waiterrobot.shared.core.CommonApp
import org.datepollsystems.waiterrobot.shared.core.data.api.ApiException
import org.datepollsystems.waiterrobot.shared.core.navigation.NavOrViewModelEffect
import org.datepollsystems.waiterrobot.shared.core.navigation.Screen
import org.datepollsystems.waiterrobot.shared.core.viewmodel.AbstractViewModel
import org.datepollsystems.waiterrobot.shared.core.viewmodel.ViewState
import org.datepollsystems.waiterrobot.shared.features.auth.repository.AuthRepository
import org.datepollsystems.waiterrobot.shared.generated.localization.L
import org.datepollsystems.waiterrobot.shared.generated.localization.alreadyLoggedIn
import org.datepollsystems.waiterrobot.shared.generated.localization.desc
import org.datepollsystems.waiterrobot.shared.generated.localization.invalid
import org.datepollsystems.waiterrobot.shared.generated.localization.title
import org.datepollsystems.waiterrobot.shared.utils.DeepLink
import org.orbitmvi.orbit.syntax.simple.SimpleSyntax
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.syntax.simple.repeatOnSubscription
import kotlin.time.Duration.Companion.seconds

class RootViewModel internal constructor(
    private val authRepo: AuthRepository,
    private val rootApi: RootApi
) : AbstractViewModel<RootState, RootEffect>(RootState()) {

    override suspend fun SimpleSyntax<RootState, NavOrViewModelEffect<RootEffect>>.onCreate() {
        repeatOnSubscription {
            launch { watchLoginState() }
            launch { watchSelectedEventState() }
            launch { watchAppTheme() }
        }

        // Check the app version at each startup
        checkAppVersion()
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

    private suspend fun SimpleSyntax<RootState, NavOrViewModelEffect<RootEffect>>.onAuthDeeplink(
        deepLink: DeepLink.Auth
    ) {
        if (CommonApp.isLoggedIn.value) {
            // TODO temporary fix, on android directly after start collectSideEffect is cancelled
            //  and relaunched, therefor the snackbar would be also cancelled.
            //  -> find a better solution (google does not recommend side effects
            delay(1.seconds)
            postSideEffect(RootEffect.ShowSnackBar(L.deepLink.alreadyLoggedIn()))
            return
        }

        reduce { state.withViewState(ViewState.Loading) }

        try {
            when (deepLink) {
                is DeepLink.Auth.LoginLink -> authRepo.loginWithToken(deepLink.token)
                is DeepLink.Auth.RegisterLink -> {
                    navigator.push(Screen.RegisterScreen(deepLink.token))
                }
            }
            reduce { state.withViewState(ViewState.Idle) }
        } catch (e: CancellationException) {
            throw e
        } catch (_: ApiException.CredentialsIncorrect) {
            reduceError(L.root.invalidLoginLink.title(), L.root.invalidLoginLink.desc())
        }
    }

    private suspend fun SimpleSyntax<RootState, NavOrViewModelEffect<RootEffect>>.watchLoginState() {
        CommonApp.isLoggedIn.collect { loggedIn ->
            reduce { state.copy(isLoggedIn = loggedIn) }
            if (!loggedIn) {
                navigator.popUpToRoot()
            }
        }
    }

    private suspend fun SimpleSyntax<RootState, NavOrViewModelEffect<RootEffect>>.watchSelectedEventState() {
        CommonApp.hasEventSelected.collect { hasEventSelected ->
            reduce { state.copy(hasEventSelected = hasEventSelected) }
            if (!hasEventSelected) {
                navigator.popUpToRoot()
            }
        }
    }

    private suspend fun SimpleSyntax<RootState, NavOrViewModelEffect<RootEffect>>.watchAppTheme() {
        CommonApp.appTheme.collect {
            reduce { state.copy(selectedTheme = it) }
        }
    }

    private suspend fun checkAppVersion() {
        // Just call the index route to verify that the current app version is still supported
        rootApi.ping()
    }
}
