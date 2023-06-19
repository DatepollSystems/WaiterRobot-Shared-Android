package org.datepollsystems.waiterrobot.shared.features.table.api

import io.ktor.client.call.body
import org.datepollsystems.waiterrobot.shared.core.CommonApp
import org.datepollsystems.waiterrobot.shared.core.api.AuthorizedApi
import org.datepollsystems.waiterrobot.shared.core.api.AuthorizedClient
import org.datepollsystems.waiterrobot.shared.features.table.api.models.TableResponseDto

internal class TableApi(client: AuthorizedClient) : AuthorizedApi("waiter/table", client) {

    suspend fun getTables() = get("/", "eventId" to CommonApp.settings.selectedEventId.toString())
        .body<List<TableResponseDto>>()
}
