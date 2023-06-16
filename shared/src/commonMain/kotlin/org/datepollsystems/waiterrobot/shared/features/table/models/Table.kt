package org.datepollsystems.waiterrobot.shared.features.table.models

import kotlinx.serialization.Serializable

@Serializable // needed for android navigation
data class Table(
    val id: Long,
    val number: Long,
    val groupName: String
)
