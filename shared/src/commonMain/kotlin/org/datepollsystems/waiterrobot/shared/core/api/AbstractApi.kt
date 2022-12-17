package org.datepollsystems.waiterrobot.shared.core.api

import co.touchlab.kermit.Logger
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import org.datepollsystems.waiterrobot.shared.core.di.injectLoggerForClass
import org.koin.core.component.KoinComponent

internal abstract class AbstractApi(basePath: String, private val client: HttpClient) :
    KoinComponent {
    protected val logger: Logger by injectLoggerForClass()

    // Make sure that the baseUrl ends with a "/"
    private val baseUrl =
        "https://lava.kellner.team/api/v1/${basePath.removePrefix("/").removeSuffix("/")}/"

    /**
     * prepend string (endpoint) with base and make sure that endpoint does not start with "/"
     */
    private fun String.toFullUrl() = baseUrl + this.removePrefix("/")

    protected suspend fun get(
        endpoint: String = "",
        vararg query: Pair<String, Any>,
        block: (HttpRequestBuilder.() -> Unit)? = null
    ): HttpResponse = client.get(endpoint.toFullUrl()) {
        query.forEach {
            url.parameters.append(it.first, it.second.toString())
        }

        block?.invoke(this)
    }

    protected suspend fun post(
        endpoint: String = "",
        body: RequestBodyDto? = null,
        block: (HttpRequestBuilder.() -> Unit)? = null
    ): HttpResponse = client.post(endpoint.toFullUrl()) {
        if (body != null) {
            contentType(ContentType.Application.Json)
            setBody(body)
        }

        block?.invoke(this)
    }
}

// Marker interface
internal interface RequestBodyDto
