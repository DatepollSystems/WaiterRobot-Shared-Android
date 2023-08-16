package org.datepollsystems.waiterrobot.shared.features.switchevent.api.models

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
internal data class EventResponseDto(
    val id: Long,
    val name: String,
    val date: LocalDate? = null,
    val street: String,
    val city: String,
    val streetNumber: String,
    val postalCode: String,
    val organisationId: Long
)
