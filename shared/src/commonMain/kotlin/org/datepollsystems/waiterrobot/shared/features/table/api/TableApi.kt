package org.datepollsystems.waiterrobot.shared.features.table.api

import io.ktor.client.*
import io.ktor.client.call.*
import org.datepollsystems.waiterrobot.shared.core.CommonApp
import org.datepollsystems.waiterrobot.shared.core.api.AbstractApi
import org.datepollsystems.waiterrobot.shared.features.table.api.models.TableResponseDto

internal class TableApi(client: HttpClient) : AbstractApi("waiter/table", client) {

    suspend fun getTables() = get("/", "eventId" to CommonApp.settings.selectedEventId.toString())
        .body<List<TableResponseDto>>()
}
