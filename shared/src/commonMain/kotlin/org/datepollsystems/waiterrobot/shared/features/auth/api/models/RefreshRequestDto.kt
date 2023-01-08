package org.datepollsystems.waiterrobot.shared.features.auth.api.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.datepollsystems.waiterrobot.shared.core.api.RequestBodyDto

@Serializable
internal class RefreshRequestDto(val sessionToken: String, val sessionName: String) : RequestBodyDto

@Serializable
internal class RefreshResponseDto(
    @SerialName("token") val accessToken: String,
    val sessionToken: String? = null
)
