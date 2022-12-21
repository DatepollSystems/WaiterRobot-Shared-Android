package org.datepollsystems.waiterrobot.shared.features.switchevent.api

import io.ktor.client.*
import io.ktor.client.call.*
import org.datepollsystems.waiterrobot.shared.core.api.AbstractApi
import org.datepollsystems.waiterrobot.shared.features.switchevent.api.models.EventResponseDto

internal class EventLocationApi(client: HttpClient) : AbstractApi("waiter/event", client) {

    suspend fun getEvents() = get("/").body<List<EventResponseDto>>()
}
