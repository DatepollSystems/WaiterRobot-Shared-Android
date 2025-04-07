package org.datepollsystems.waiterrobot.shared.features.auth.api

import io.ktor.client.call.body
import org.datepollsystems.waiterrobot.shared.core.data.remote.AuthorizedApi
import org.datepollsystems.waiterrobot.shared.core.data.remote.AuthorizedClient
import org.datepollsystems.waiterrobot.shared.features.auth.api.models.WaiterDto

internal class WaiterApi(client: AuthorizedClient) : AuthorizedApi("v1/waiter", client) {

    suspend fun getMySelf() = get("myself").body<WaiterDto>()
}
