package org.datepollsystems.waiterrobot.shared.root

import io.ktor.client.HttpClient
import org.datepollsystems.waiterrobot.shared.core.data.api.AbstractApi

internal class RootApi(client: HttpClient) : AbstractApi(basePath = "/", client) {
    suspend fun ping() {
        get("/")
    }
}
