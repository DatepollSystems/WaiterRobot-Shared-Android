package org.datepollsystems.waiterrobot.shared.features.auth.api.models

import kotlinx.serialization.Serializable

@Serializable
internal data class WaiterDto(
    val id: Long,
    val name: String,
    val organisationId: Long,
    val organisationName: String,
    val eventIds: List<Long>,
)
