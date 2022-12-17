package org.datepollsystems.waiterrobot.shared.core.api

import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.statement.*
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
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
            println(jsonString)
            throw try {
                json.decodeFromString<ApiException>(jsonString)
            } catch (e: SerializationException) {
                println("Could not serialize ClientError using fallback: $e")
                json.decodeFromString<ApiException.Generic>(jsonString)
            }
        }
    }
}
