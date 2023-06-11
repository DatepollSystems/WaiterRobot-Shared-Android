package org.datepollsystems.waiterrobot.shared.core

import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerAuthProvider
import io.ktor.client.plugins.pluginOrNull
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.datepollsystems.waiterrobot.shared.core.api.AuthorizedClient
import org.datepollsystems.waiterrobot.shared.core.di.injectLoggerForClass
import org.datepollsystems.waiterrobot.shared.core.settings.SharedSettings
import org.datepollsystems.waiterrobot.shared.features.auth.api.AuthApi
import org.datepollsystems.waiterrobot.shared.features.settings.models.AppTheme
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object CommonApp : KoinComponent {
    private val coroutineScope: CoroutineScope by inject()
    private val logger by injectLoggerForClass()
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
        coroutineScope.launch {
            try {
                val tokens = settings.tokens ?: return@launch
                getKoin().getOrNull<AuthApi>()?.logout(tokens)
            } catch (e: Exception) {
                logger.e(e) { "Could not delete session." }
            }
        }

        settings.tokens = null // This also triggers a change to the isLoggedInFlow
        settings.selectedEventId = -1
        settings.eventName = ""
        settings.organisationName = ""
        settings.waiterName = ""

        // Clear the tokens from the client, so that they get reloaded.
        val apiClients = getKoin().getAll<AuthorizedClient>()
        apiClients.forEach {
            it.delegate.pluginOrNull(Auth)
                ?.providers
                ?.filterIsInstance<BearerAuthProvider>()
                ?.forEach(BearerAuthProvider::clearToken)
        }
    }
}
