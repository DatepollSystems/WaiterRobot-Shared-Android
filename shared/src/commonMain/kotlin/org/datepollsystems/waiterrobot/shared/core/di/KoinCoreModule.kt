package org.datepollsystems.waiterrobot.shared.core.di

import co.touchlab.kermit.Logger
import co.touchlab.kermit.Severity
import co.touchlab.kermit.StaticConfig
import co.touchlab.kermit.platformLogWriter
import io.ktor.client.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.serialization.json.Json
import org.datepollsystems.waiterrobot.shared.core.api.createApiClient
import org.datepollsystems.waiterrobot.shared.core.db.createRealmDB
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope
import org.koin.dsl.module

private val apiClientQualifier = named("apiClient")

fun Scope.getApiClient(): HttpClient {
    return get(HttpClient::class, apiClientQualifier)
}

internal val coreModule = module {
    val baseLogger = Logger(
        // TODO different severity for debug and release build?
        StaticConfig(Severity.Verbose, logWriterList = listOf(platformLogWriter())),
        tag = "WaiterRobot"
    )
    factory { (tag: String?) -> if (tag != null) baseLogger.withTag(tag) else baseLogger }

    single { createJson() }
    single(apiClientQualifier) {
        createApiClient(
            json = get(),
            ktorLogger = CustomKtorLogger("api"),
            authRepository = get(),
            enableNetworkLogs = true
        )
    }

    single { createRealmDB() }

    single { CoroutineScope(SupervisorJob() + Dispatchers.Default) }
}

private fun createJson() = Json { ignoreUnknownKeys = true }
