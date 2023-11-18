package org.datepollsystems.waiterrobot.shared.features.table.api.models

import kotlinx.serialization.Serializable

@Serializable
internal data class TableGroupResponseDto(
    val id: Long,
    val eventId: Long,
    val name: String,
    val position: Int = Int.MAX_VALUE,
    val color: String?,
    val tables: List<TableResponseDto>,
)

@Serializable
internal data class TableResponseDto(
    val id: Long,
    val number: Int,
)

@Serializable
internal data class TableIdsWithActiveOrdersResponseDto(
    val tableIds: Set<Long>
)
