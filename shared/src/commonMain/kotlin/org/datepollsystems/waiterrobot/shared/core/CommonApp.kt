package org.datepollsystems.waiterrobot.shared.core

import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import org.datepollsystems.waiterrobot.shared.core.settings.SharedSettings
import org.koin.core.component.KoinComponent

object CommonApp : KoinComponent {
    val settings = SharedSettings()

    internal val isLoggedIn: Boolean get() = settings.tokens != null
    internal val isLoggedInFlow: Flow<Boolean> =
        settings.tokenFlow.map { it != null }.distinctUntilChanged()

    internal fun logout() {
        settings.tokens = null // This also triggers a change to the isLoggedInFlow

        // Clear the tokens from the client, so that they get reloaded.
        val apiClients = getKoin().getAll<HttpClient>()
        apiClients.forEach {
            it.plugin(Auth).providers.filterIsInstance<BearerAuthProvider>()
                .forEach(BearerAuthProvider::clearToken)
        }
    }
}
