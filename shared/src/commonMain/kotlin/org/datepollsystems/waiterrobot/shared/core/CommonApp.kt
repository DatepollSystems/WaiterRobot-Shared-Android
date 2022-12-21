package org.datepollsystems.waiterrobot.shared.core

import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.datepollsystems.waiterrobot.shared.core.settings.SharedSettings
import org.koin.core.component.KoinComponent

object CommonApp : KoinComponent {
    val appScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    val settings = SharedSettings()

    internal val isLoggedIn: StateFlow<Boolean> = settings.tokenFlow
        .map { it != null }
        .stateIn(appScope, SharingStarted.Lazily, settings.tokens != null)

    internal val hasEventSelected: StateFlow<Boolean> = settings.selectedEventIdFlow
        .map { it != -1L }
        .stateIn(appScope, started = SharingStarted.Lazily, settings.selectedEventId != -1L)

    fun logout() {
        settings.tokens = null // This also triggers a change to the isLoggedInFlow
        settings.selectedEventId = -1
        settings.eventName = ""
        settings.organisationName = ""
        settings.waiterName = ""

        // Clear the tokens from the client, so that they get reloaded.
        val apiClients = getKoin().getAll<HttpClient>()
        apiClients.forEach {
            it.pluginOrNull(Auth)
                ?.providers
                ?.filterIsInstance<BearerAuthProvider>()
                ?.forEach(BearerAuthProvider::clearToken)
        }
    }
}
