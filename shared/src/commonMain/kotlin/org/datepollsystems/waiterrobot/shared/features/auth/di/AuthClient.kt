package org.datepollsystems.waiterrobot.shared.features.auth.di

import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.datepollsystems.waiterrobot.shared.core.AppInfo
import org.datepollsystems.waiterrobot.shared.core.api.installApiClientExceptionTransformer
import io.ktor.client.plugins.logging.Logger as KtorLogger

internal fun createAuthClient(
    json: Json,
    logger: KtorLogger,
    enableNetworkLogs: Boolean
) = HttpClient {
    install(ContentNegotiation) {
        json(json)
    }
    install(HttpTimeout) {
        requestTimeoutMillis = 10_000
    }

    if (enableNetworkLogs) {
        install(Logging) {
            this.logger = logger
            level = LogLevel.ALL
        }
    }

    defaultRequest {
        header("X-App-Version", AppInfo.appVersion)
        header("X-App-Os", AppInfo.os.toString())
    }

    installApiClientExceptionTransformer(json)
}
