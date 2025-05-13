package org.datepollsystems.waiterrobot.shared.features.auth.api.models

import kotlinx.serialization.Serializable
import org.datepollsystems.waiterrobot.shared.core.data.remote.RequestBodyDto

@Serializable
internal data class LogoutRequestDto(val refreshToken: String) : RequestBodyDto
