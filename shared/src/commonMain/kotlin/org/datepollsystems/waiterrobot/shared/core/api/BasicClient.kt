package org.datepollsystems.waiterrobot.shared.core.api

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.datepollsystems.waiterrobot.shared.core.CommonApp
import io.ktor.client.plugins.logging.Logger as KtorLogger

internal fun createBasicClient(
    json: Json,
    logger: KtorLogger,
    enableNetworkLogs: Boolean
) = HttpClient {
    commonConfig(json, logger, enableNetworkLogs)
}

internal fun HttpClientConfig<*>.commonConfig(
    json: Json,
    ktorLogger: KtorLogger,
    enableNetworkLogs: Boolean
) {
    install(ContentNegotiation) {
        json(json)
    }

    install(HttpTimeout) {
        requestTimeoutMillis = 10_000
    }

    if (enableNetworkLogs) {
        install(Logging) {
            logger = ktorLogger
            level = LogLevel.ALL
        }
    }

    defaultRequest {
        header("X-App-Version", CommonApp.appInfo.appVersion)
        header("X-App-Os", CommonApp.appInfo.os.toString())
        header("X-App-Name", "Mobile")
    }

    installApiClientExceptionTransformer(json)
}
