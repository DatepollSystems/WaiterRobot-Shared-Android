package org.datepollsystems.waiterrobot.shared.root.data.remote

import io.ktor.client.HttpClient
import org.datepollsystems.waiterrobot.shared.core.data.remote.AbstractApi

internal class RootApi(client: HttpClient) : AbstractApi(basePath = "v1/", client) {
    suspend fun ping() {
        get("/")
    }
}
