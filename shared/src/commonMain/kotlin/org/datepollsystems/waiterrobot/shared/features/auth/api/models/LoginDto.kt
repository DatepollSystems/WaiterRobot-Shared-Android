package org.datepollsystems.waiterrobot.shared.features.auth.api.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.datepollsystems.waiterrobot.shared.core.api.RequestBodyDto

@Serializable
internal class LoginRequestDto(
    val token: String,
    val sessionInformation: String,
    val stayLoggedIn: Boolean = true
) : RequestBodyDto

@Serializable
internal class LoginResponseDto(
    val accessToken: String,
    val refreshToken: String
)

@Serializable
internal class CreateLoginRequestDto(
    @SerialName("name") val waiterName: String,
    val waiterCreateToken: String,
    val sessionInformation: String
) : RequestBodyDto
