package org.datepollsystems.waiterrobot.shared.features.switchevent.models

import kotlinx.datetime.LocalDate

data class Event(
    val id: Long,
    val name: String,
    val date: LocalDate? = null,
    val city: String,
    val organisationId: Long
)
