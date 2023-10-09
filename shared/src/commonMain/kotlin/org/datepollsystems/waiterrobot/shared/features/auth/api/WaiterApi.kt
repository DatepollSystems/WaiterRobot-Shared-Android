package org.datepollsystems.waiterrobot.shared.features.auth.api

import io.ktor.client.call.body
import org.datepollsystems.waiterrobot.shared.core.data.api.AuthorizedApi
import org.datepollsystems.waiterrobot.shared.core.data.api.AuthorizedClient
import org.datepollsystems.waiterrobot.shared.features.auth.api.models.WaiterDto

internal class WaiterApi(client: AuthorizedClient) : AuthorizedApi("waiter", client) {

    suspend fun getMySelf() = get("myself").body<WaiterDto>()
}
