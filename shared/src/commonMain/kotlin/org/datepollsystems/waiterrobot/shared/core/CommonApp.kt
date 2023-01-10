package org.datepollsystems.waiterrobot.shared.core

import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.datepollsystems.waiterrobot.shared.core.settings.SharedSettings
import org.datepollsystems.waiterrobot.shared.features.settings.models.AppTheme
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object CommonApp : KoinComponent {
    private val coroutineScope: CoroutineScope by inject()
    val settings by lazy { SharedSettings() }

    const val privacyPolicyUrl: String = "https://my.kellner.team/info/mobile-privacypolicy"

    lateinit var appInfo: AppInfo
        private set

    fun init(appVersion: String, appBuild: Int, phoneModel: String, os: OS, apiBaseUrl: String) {
        this.appInfo = AppInfo(appVersion, appBuild, phoneModel, os, apiBaseUrl)
    }

    internal val isLoggedIn: StateFlow<Boolean> by lazy {
        settings.tokenFlow
            .map { it != null }
            .stateIn(coroutineScope, SharingStarted.Lazily, settings.tokens != null)
    }

    internal val hasEventSelected: StateFlow<Boolean> by lazy {
        settings.selectedEventIdFlow
            .map { it != -1L }
            .stateIn(
                coroutineScope,
                started = SharingStarted.Lazily,
                settings.selectedEventId != -1L
            )
    }

    internal val appTheme: StateFlow<AppTheme> by lazy {
        settings.appThemeFlow
            .stateIn(coroutineScope, started = SharingStarted.Lazily, settings.appTheme)
    }

    internal fun logout() {
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
