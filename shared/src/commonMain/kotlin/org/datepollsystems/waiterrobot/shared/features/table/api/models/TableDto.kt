package org.datepollsystems.waiterrobot.shared.features.table.api.models

import kotlinx.serialization.Serializable

@Serializable
internal data class TableResponseDto(
    val id: Long,
    val number: Long,
    val seats: Long,
    val groupId: Long,
    val groupName: String,
    val eventId: Long
)
