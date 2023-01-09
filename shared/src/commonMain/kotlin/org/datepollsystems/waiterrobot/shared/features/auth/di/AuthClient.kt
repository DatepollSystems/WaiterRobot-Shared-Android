package org.datepollsystems.waiterrobot.shared.features.auth.di

import io.ktor.client.*
import kotlinx.serialization.json.Json
import org.datepollsystems.waiterrobot.shared.core.api.commonConfig
import io.ktor.client.plugins.logging.Logger as KtorLogger

internal fun createAuthClient(
    json: Json,
    logger: KtorLogger,
    enableNetworkLogs: Boolean
) = HttpClient {
    commonConfig(json, logger, enableNetworkLogs)
}
