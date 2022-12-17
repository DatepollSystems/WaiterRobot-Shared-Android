package org.datepollsystems.waiterrobot.shared.features.auth.repository

import org.datepollsystems.waiterrobot.shared.core.CommonApp
import org.datepollsystems.waiterrobot.shared.core.repository.AbstractRepository
import org.datepollsystems.waiterrobot.shared.core.settings.Tokens
import org.datepollsystems.waiterrobot.shared.features.auth.api.AuthApi
import org.datepollsystems.waiterrobot.shared.features.auth.api.WaiterApi

internal class AuthRepository(private val authApi: AuthApi, private val waiterApi: WaiterApi) :
    AbstractRepository() {

    suspend fun loginWithToken(token: String) {
        val tokens = Tokens.fromLoginResponse(
            authApi.loginWithToken(token)
        )

        store(tokens)
    }

    suspend fun createWithToken(token: String, waiterName: String) {
        val tokens = Tokens.fromLoginResponse(
            authApi.createWithToken(token, waiterName)
        )

        store(tokens)
    }

    suspend fun refreshTokens(): Tokens? {
        var tokens = getTokens()

        if (tokens == null) {
            CommonApp.logout()
            return null
        }

        tokens = Tokens(authApi.refreshToken(tokens.refreshToken).accessToken, tokens.refreshToken)

        store(tokens)

        return tokens
    }

    fun getTokens(): Tokens? {
        return CommonApp.settings.tokens
    }

    private suspend fun store(tokens: Tokens) {
        CommonApp.settings.tokens = tokens

        waiterApi.getMySelf().let {
            CommonApp.settings.organisationName = it.organisationName
            CommonApp.settings.waiterName = it.name
        }

        logger.d { "Saved tokens ${CommonApp.settings.tokens}" }
    }
}
