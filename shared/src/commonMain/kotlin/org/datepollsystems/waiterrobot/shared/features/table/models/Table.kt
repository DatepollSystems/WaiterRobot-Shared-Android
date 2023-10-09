package org.datepollsystems.waiterrobot.shared.features.table.models

import kotlinx.serialization.Serializable

@Serializable // needed for android navigation
data class Table(
    val id: Long,
    val number: Int,
    val groupName: String = "TODO", // TODO
    val hasOrders: Boolean = false
)
