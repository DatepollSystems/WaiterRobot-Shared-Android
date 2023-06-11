package org.datepollsystems.waiterrobot.shared.core.api

import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import kotlinx.coroutines.CoroutineScope
import kotlinx.serialization.json.Json
import org.datepollsystems.waiterrobot.shared.core.CommonApp
import org.datepollsystems.waiterrobot.shared.core.settings.Tokens
import org.datepollsystems.waiterrobot.shared.features.auth.repository.AuthRepository
import kotlin.jvm.JvmInline
import io.ktor.client.plugins.logging.Logger as KtorLogger

@JvmInline
value class AuthorizedClient(val delegate: HttpClient)

internal fun createAuthorizedClient(
    json: Json,
    ktorLogger: KtorLogger,
    authRepository: AuthRepository,
    enableNetworkLogs: Boolean = false,
    scope: CoroutineScope
): AuthorizedClient = AuthorizedClient(
    HttpClient {
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
)

private fun Tokens?.toBearerTokens(): BearerTokens? = this?.let {
    BearerTokens(it.accessToken, it.refreshToken)
}
