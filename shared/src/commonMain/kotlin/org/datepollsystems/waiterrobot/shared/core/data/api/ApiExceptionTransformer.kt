package org.datepollsystems.waiterrobot.shared.core.data.api

import co.touchlab.kermit.Logger
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json

internal fun HttpClientConfig<*>.installApiClientExceptionTransformer(json: Json) {
    expectSuccess = true
    HttpResponseValidator {
        handleResponseExceptionWithRequest { exception, request ->
            // TODO maybe also map ServerExceptions (and also other HttpExceptions) to ApiException
            val clientException = exception as? ClientRequestException
                ?: return@handleResponseExceptionWithRequest

            val logger = Logger.withTag("ApiClientExceptionTransformer")

            // Get as string and do custom serialization here, so we can fallback to a generic error
            // with the basic error information if the client does not know the codeName.
            val jsonString = clientException.response.bodyAsText()
            throw try {
                json.decodeFromString<ApiException>(jsonString)
            } catch (e: SerializationException) {
                logger.w(e) {
                    "Could not serialize ApiException. Falling back to generic ApiException. " +
                        "(${request.method} ${request.url})"
                }
                json.decodeFromString<ApiException.Generic>(jsonString)
            }
        }
    }
}
