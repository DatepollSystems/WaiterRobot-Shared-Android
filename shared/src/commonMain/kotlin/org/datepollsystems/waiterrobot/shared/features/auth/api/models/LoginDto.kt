package org.datepollsystems.waiterrobot.shared.features.auth.api.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.datepollsystems.waiterrobot.shared.core.api.RequestBodyDto

@Serializable
internal class LoginRequestDto(
    val token: String,
    @SerialName("sessionInformation") val sessionName: String
) : RequestBodyDto

@Serializable
internal class LoginResponseDto(
    @SerialName("token") val accessToken: String,
    val sessionToken: String
)

@Serializable
internal data class CreateLoginRequestDto(
    @SerialName("name") val waiterName: String,
    val waiterCreateToken: String,
    @SerialName("sessionInformation") val sessionName: String
) : RequestBodyDto
