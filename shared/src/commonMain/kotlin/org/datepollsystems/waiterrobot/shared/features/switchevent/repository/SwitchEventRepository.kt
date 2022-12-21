package org.datepollsystems.waiterrobot.shared.features.switchevent.repository

import org.datepollsystems.waiterrobot.shared.core.CommonApp
import org.datepollsystems.waiterrobot.shared.core.repository.AbstractRepository
import org.datepollsystems.waiterrobot.shared.features.switchevent.api.EventLocationApi
import org.datepollsystems.waiterrobot.shared.features.switchevent.models.Event

internal class SwitchEventRepository(private val eventLocationApi: EventLocationApi) :
    AbstractRepository() {

    suspend fun getEvents(): List<Event> = eventLocationApi.getEvents().map {
        Event(it.id, it.name, it.date, it.city, it.organisationId)
    }

    fun switchToEvent(event: Event) {
        CommonApp.settings.selectedEventId = event.id
        CommonApp.settings.eventName = event.name
    }
}
