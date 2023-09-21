package org.datepollsystems.waiterrobot.shared.features.auth.api.models

import kotlinx.serialization.Serializable
import org.datepollsystems.waiterrobot.shared.core.api.RequestBodyDto

@Serializable
internal data class LogoutRequestDto(val refreshToken: String) : RequestBodyDto
