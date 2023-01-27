package org.datepollsystems.waiterrobot.shared.features.auth.api

import io.ktor.client.*
import io.ktor.client.call.*
import org.datepollsystems.waiterrobot.shared.core.CommonApp
import org.datepollsystems.waiterrobot.shared.core.api.AbstractApi
import org.datepollsystems.waiterrobot.shared.core.settings.Tokens
import org.datepollsystems.waiterrobot.shared.features.auth.api.models.*

internal class AuthApi(client: HttpClient) : AbstractApi("waiter/auth", client) {

    suspend fun refreshToken(sessionToken: String) = post(
        endpoint = "refresh",
        body = RefreshRequestDto(sessionToken, CommonApp.appInfo.sessionName)
    ).body<RefreshResponseDto>()

    suspend fun loginWithToken(token: String) = post(
        endpoint = "login",
        body = LoginRequestDto(token, CommonApp.appInfo.sessionName)
    ).body<LoginResponseDto>()

    suspend fun createWithToken(token: String, waiterName: String) = post(
        endpoint = "loginWithCreateToken",
        body = CreateLoginRequestDto(waiterName, token, CommonApp.appInfo.sessionName)
    ).body<LoginResponseDto>()

    suspend fun logout(tokens: Tokens) {
        post(endpoint = "logout", body = LogoutRequestDto(tokens.refreshToken))
    }
}
