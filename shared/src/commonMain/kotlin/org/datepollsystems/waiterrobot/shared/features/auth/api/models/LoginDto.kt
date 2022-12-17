package org.datepollsystems.waiterrobot.shared.features.auth.api.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal class LoginRequestDto(
    val token: String,
    @SerialName("sessionInformation") val sessionName: String
)

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
)
