package org.datepollsystems.waiterrobot.shared.features.switchevent.data.remote

import io.ktor.client.call.body
import org.datepollsystems.waiterrobot.shared.core.data.api.AuthorizedApi
import org.datepollsystems.waiterrobot.shared.core.data.api.AuthorizedClient
import org.datepollsystems.waiterrobot.shared.features.switchevent.data.remote.dto.EventResponseDto

internal class EventLocationApi(client: AuthorizedClient) :
    AuthorizedApi("v1/waiter/event", client) {

    suspend fun getEvents() = get("/").body<List<EventResponseDto>>()
}
