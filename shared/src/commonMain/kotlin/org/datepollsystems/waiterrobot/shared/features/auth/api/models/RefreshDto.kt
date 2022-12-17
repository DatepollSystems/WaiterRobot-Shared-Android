package org.datepollsystems.waiterrobot.shared.features.auth.api.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal class RefreshDto(val sessionToken: String, val sessionName: String)

@Serializable
internal class RefreshResponseDto(
    @SerialName("token") val accessToken: String
)
