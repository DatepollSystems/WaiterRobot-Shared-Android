package org.datepollsystems.waiterrobot.shared.features.switchevent.repository

import org.datepollsystems.waiterrobot.shared.core.CommonApp
import org.datepollsystems.waiterrobot.shared.core.repository.AbstractRepository
import org.datepollsystems.waiterrobot.shared.features.switchevent.api.EventLocationApi
import org.datepollsystems.waiterrobot.shared.features.switchevent.models.Event

internal class SwitchEventRepository(private val eventLocationApi: EventLocationApi) :
    AbstractRepository() {

    suspend fun getEvents(): List<Event> = eventLocationApi.getEvents().map {
        Event(
            id = it.id,
            name = it.name,
            startDate = it.startDate,
            endDate = it.endDate,
            city = it.city,
            organisationId = it.organisationId,
            stripeEnabled = it.stripeEnabled,
            stripeMinAmount = it.stripeMinAmount
        )
    }

    fun switchToEvent(event: Event) {
        CommonApp.settings.selectedEvent = event

        // TODO extract, this must be also callable from other places without switching the event
        // TODO probably we will also need to regularly fetch the event to check for updated config
        if (event.stripeEnabled) {
            // TODO connect to local reader
        } else {
            // TODO close local reader connection
        }
    }
}
