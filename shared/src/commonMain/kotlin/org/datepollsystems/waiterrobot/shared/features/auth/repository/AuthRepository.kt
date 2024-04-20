package org.datepollsystems.waiterrobot.shared.features.auth.repository

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.datepollsystems.waiterrobot.shared.core.CommonApp
import org.datepollsystems.waiterrobot.shared.core.repository.AbstractRepository
import org.datepollsystems.waiterrobot.shared.core.settings.Tokens
import org.datepollsystems.waiterrobot.shared.features.auth.api.AuthApi
import org.datepollsystems.waiterrobot.shared.features.auth.api.WaiterApi
import org.datepollsystems.waiterrobot.shared.features.switchevent.repository.SwitchEventRepository
import org.datepollsystems.waiterrobot.shared.utils.DeepLink
import org.koin.core.component.inject
import kotlin.coroutines.cancellation.CancellationException

internal class AuthRepository(private val authApi: AuthApi) : AbstractRepository() {

    // Use inject for lazy loading to prevent circular init dependency (apiClient -> AuthRepo -> apiClient -> ...)
    private val waiterApi: WaiterApi by inject()
    private val eventRepository: SwitchEventRepository by inject()

    suspend fun loginWaiter(deepLink: DeepLink.Auth.LoginLink) {
        CommonApp.settings.apiBase = deepLink.apiBase

        val tokens = Tokens.fromLoginResponse(
            authApi.loginWithToken(deepLink.token)
        )

        store(tokens)
        autoSelectEvent()
    }

    suspend fun createWaiter(deepLink: DeepLink.Auth.RegisterLink, waiterName: String) {
        CommonApp.settings.apiBase = deepLink.apiBase

        val tokens = Tokens.fromLoginResponse(
            authApi.createWithToken(deepLink.token, waiterName)
        )

        store(tokens)
        autoSelectEvent()
    }

    suspend fun refreshTokens(userDetailsRefreshScope: CoroutineScope): Tokens? {
        var tokens = getTokens()

        if (tokens == null) {
            CommonApp.logout()
            return null
        }

        val newTokens = authApi.refreshToken(tokens.refreshToken)
        tokens = Tokens(newTokens.accessToken, newTokens.refreshToken ?: tokens.refreshToken)

        store(tokens, userDetailsRefreshScope)

        return tokens
    }

    fun getTokens(): Tokens? {
        return CommonApp.settings.tokens
    }

    private suspend fun autoSelectEvent() {
        @Suppress("TooGenericExceptionCaught")
        try {
            // Auto select event when there is only one available
            eventRepository.getEvents().singleOrNull()?.let {
                eventRepository.switchToEvent(it)
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            logger.w(e) { "Auto-selecting event failed" }
        }
    }

    private suspend fun store(tokens: Tokens, userDetailsRefreshScope: CoroutineScope? = null) {
        suspend fun refreshUserDetails() {
            logger.d { "Refreshing user details" }
            waiterApi.getMySelf().let {
                // TODO move this logic to an "service"?
                //  Changing the stripe location id should also refresh the terminal connection
                CommonApp.settings.organisationName = it.organisationName
                CommonApp.settings.waiterName = it.name
            }
        }

        CommonApp.settings.tokens = tokens

        if (userDetailsRefreshScope != null) {
            // Update in the background, as this call will suspend till refreshTokens finish (-> deadlock)
            userDetailsRefreshScope.launch {
                refreshUserDetails()
            }
        } else {
            refreshUserDetails()
        }

        logger.d { "Saved tokens" }
    }
}
