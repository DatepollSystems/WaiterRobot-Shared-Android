package org.datepollsystems.waiterrobot.shared.features.auth.api

import io.ktor.client.*
import io.ktor.client.call.*
import org.datepollsystems.waiterrobot.shared.core.api.AbstractApi
import org.datepollsystems.waiterrobot.shared.features.auth.api.models.WaiterDto

internal class WaiterApi(client: HttpClient) : AbstractApi("waiter", client) {

    suspend fun getMySelf() = get("myself").body<WaiterDto>()
}
