package org.datepollsystems.waiterrobot.shared.features.auth.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import org.datepollsystems.waiterrobot.shared.core.CommonApp
import org.datepollsystems.waiterrobot.shared.core.data.api.AbstractApi
import org.datepollsystems.waiterrobot.shared.core.settings.Tokens
import org.datepollsystems.waiterrobot.shared.features.auth.api.models.CreateLoginRequestDto
import org.datepollsystems.waiterrobot.shared.features.auth.api.models.LoginRequestDto
import org.datepollsystems.waiterrobot.shared.features.auth.api.models.LoginResponseDto
import org.datepollsystems.waiterrobot.shared.features.auth.api.models.LogoutRequestDto
import org.datepollsystems.waiterrobot.shared.features.auth.api.models.RefreshRequestDto
import org.datepollsystems.waiterrobot.shared.features.auth.api.models.RefreshResponseDto

internal class AuthApi(client: HttpClient) : AbstractApi("v1/waiter/auth", client) {

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
