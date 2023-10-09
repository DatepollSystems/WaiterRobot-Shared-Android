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
        handleResponseExceptionWithRequest { exception, _ ->
            val clientException =
                exception as? ClientRequestException ?: return@handleResponseExceptionWithRequest

            // Get as string and do custom serialization here, so we can fallback to a generic error
            // with the basic error information if the client does not know the codeName.
            val jsonString = clientException.response.bodyAsText()
            throw try {
                json.decodeFromString<ApiException>(jsonString)
            } catch (e: SerializationException) {
                Logger.withTag("ApiClientExceptionTransformer").w(e) {
                    "Could not serialize ClientError using fallback"
                }
                json.decodeFromString<ApiException.Generic>(jsonString)
            }
            // TODO handle complete invalid response (which can not be decoded as a Generic ApiException)
        }
    }
}
