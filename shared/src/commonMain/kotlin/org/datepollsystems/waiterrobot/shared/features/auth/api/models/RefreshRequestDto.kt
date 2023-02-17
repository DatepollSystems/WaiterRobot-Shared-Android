package org.datepollsystems.waiterrobot.shared.features.auth.api.models

import kotlinx.serialization.Serializable
import org.datepollsystems.waiterrobot.shared.core.api.RequestBodyDto

@Serializable
internal class RefreshRequestDto(val refreshToken: String, val sessionInformation: String) :
    RequestBodyDto

@Serializable
internal class RefreshResponseDto(
    val accessToken: String,
    val refreshToken: String? = null
)
