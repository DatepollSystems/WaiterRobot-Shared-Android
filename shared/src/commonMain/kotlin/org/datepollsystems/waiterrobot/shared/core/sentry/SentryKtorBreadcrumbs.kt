package org.datepollsystems.waiterrobot.shared.core.sentry

import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.client.statement.request
import io.sentry.kotlin.multiplatform.Sentry
import io.sentry.kotlin.multiplatform.protocol.Breadcrumb

val SentryKtorBreadcrumbsPlugin = createClientPlugin("SentryKtorBreadcrumbsPlugin") {
    onRequest { request, _ ->
        Sentry.addBreadcrumb(
            Breadcrumb.http(
                url = request.url.toString(),
                method = request.method.value,
            )
        )
    }
    onResponse { response ->
        Sentry.addBreadcrumb(
            Breadcrumb.http(
                url = response.request.url.toString(),
                method = response.request.method.value,
                code = response.status.value
            )
        )
    }
}
