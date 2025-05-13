package org.datepollsystems.waiterrobot.shared.features.switchevent.domain.repository

import org.datepollsystems.waiterrobot.shared.features.switchevent.domain.model.Event

internal interface SwitchEventRepository {
    suspend fun getEvents(): Result<List<Event>>
    suspend fun switchToEvent(event: Event): Boolean
}
