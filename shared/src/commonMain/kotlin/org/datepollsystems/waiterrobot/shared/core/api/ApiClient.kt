package org.datepollsystems.waiterrobot.shared.core.api

import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.serialization.json.Json
import org.datepollsystems.waiterrobot.shared.core.CommonApp
import org.datepollsystems.waiterrobot.shared.core.settings.Tokens
import org.datepollsystems.waiterrobot.shared.features.auth.repository.AuthRepository
import io.ktor.client.plugins.logging.Logger as KtorLogger

internal fun createApiClient(
    json: Json,
    ktorLogger: KtorLogger,
    authRepository: AuthRepository,
    enableNetworkLogs: Boolean = false,
    scope: CoroutineScope
) = HttpClient {
    commonConfig(json, ktorLogger, enableNetworkLogs)

    install(Auth) {
        bearer {
            loadTokens {
                authRepository.getTokens().toBearerTokens()
            }

            // Function to refresh a token (called when server response with 401 and a WWW-Authenticate header)
            refreshTokens {
                try {
                    authRepository.refreshTokens(scope).toBearerTokens()
                } catch (e: Exception) {
                    // TODO improve request errors handling (-> try again, no connection info)
                    ktorLogger.log(e.message ?: "Error while refreshing token")
                    CommonApp.logout()
                    null
                }
            }
        }
    }
}

fun HttpClientConfig<*>.commonConfig(
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
    }

    installApiClientExceptionTransformer(json)
}

private fun Tokens?.toBearerTokens(): BearerTokens? = this?.let {
    BearerTokens(it.accessToken, it.refreshToken)
}
