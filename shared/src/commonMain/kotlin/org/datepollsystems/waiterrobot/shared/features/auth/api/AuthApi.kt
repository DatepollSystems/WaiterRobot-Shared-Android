package org.datepollsystems.waiterrobot.shared.features.auth.api

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import org.datepollsystems.waiterrobot.shared.core.AppInfo
import org.datepollsystems.waiterrobot.shared.core.api.AbstractApi
import org.datepollsystems.waiterrobot.shared.features.auth.api.models.*

internal class AuthApi(client: HttpClient) : AbstractApi("waiter/auth", client) {

    suspend fun refreshToken(sessionToken: String) = post("refresh") {
        setBody(RefreshDto(sessionToken, AppInfo.sessionName))
    }.body<RefreshResponseDto>()

    suspend fun loginWithToken(token: String) = post("signIn") {
        setBody(LoginRequestDto(token, AppInfo.sessionName))
    }.body<LoginResponseDto>()

    suspend fun createWithToken(token: String, waiterName: String) = post("signInViaCreateToken") {
        setBody(CreateLoginRequestDto(waiterName, token, AppInfo.sessionName))
    }.body<LoginResponseDto>()
}
