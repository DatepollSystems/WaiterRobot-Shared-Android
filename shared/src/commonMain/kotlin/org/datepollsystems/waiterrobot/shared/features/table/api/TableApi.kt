package org.datepollsystems.waiterrobot.shared.features.table.api

import io.ktor.client.call.body
import org.datepollsystems.waiterrobot.shared.core.CommonApp
import org.datepollsystems.waiterrobot.shared.core.data.api.AuthorizedApi
import org.datepollsystems.waiterrobot.shared.core.data.api.AuthorizedClient
import org.datepollsystems.waiterrobot.shared.features.table.api.models.TableGroupResponseDto
import org.datepollsystems.waiterrobot.shared.features.table.api.models.TableIdsWithActiveOrdersResponseDto

internal class TableApi(client: AuthorizedClient) : AuthorizedApi("v1/waiter/table", client) {

    suspend fun getTableGroups() =
        get("group", "eventId" to CommonApp.settings.selectedEventId.toString())
            .body<List<TableGroupResponseDto>>()

    suspend fun getTableIdsOfTablesWithOpenOrder() =
        get("activeOrders", "eventId" to CommonApp.settings.selectedEventId.toString())
            .body<TableIdsWithActiveOrdersResponseDto>().tableIds
}
