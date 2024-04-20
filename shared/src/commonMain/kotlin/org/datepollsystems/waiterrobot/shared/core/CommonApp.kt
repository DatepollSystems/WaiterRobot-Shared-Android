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
import org.datepollsystems.waiterrobot.shared.core.data.api.AuthorizedClient
import org.datepollsystems.waiterrobot.shared.core.di.injectLoggerForClass
import org.datepollsystems.waiterrobot.shared.core.settings.SharedSettings
import org.datepollsystems.waiterrobot.shared.features.auth.api.AuthApi
import org.datepollsystems.waiterrobot.shared.features.billing.repository.StripeProvider
import org.datepollsystems.waiterrobot.shared.features.settings.models.AppTheme
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.coroutines.cancellation.CancellationException

object CommonApp : KoinComponent {
    private val coroutineScope: CoroutineScope by inject()
    private val logger by injectLoggerForClass()
    val settings by lazy { SharedSettings() }

    const val privacyPolicyUrl: String = "https://my.kellner.team/info/mobile-privacypolicy"

    lateinit var appInfo: AppInfo
        private set

    internal var stripeProvider: StripeProvider? = null

    fun init(
        appVersion: String,
        appBuild: Int,
        phoneModel: String,
        os: OS,
        allowedHostsCsv: String,
        stripeProvider: StripeProvider? = null
    ) {
        this.appInfo = AppInfo(appVersion, appBuild, phoneModel, os, allowedHostsCsv)
        this.stripeProvider = stripeProvider
    }

    internal val isLoggedIn: StateFlow<Boolean> by lazy {
        settings.tokenFlow
            .map { it != null }
            .stateIn(coroutineScope, SharingStarted.Lazily, settings.tokens != null)
    }

    internal val hasEventSelected: StateFlow<Boolean> by lazy {
        settings.selectedEventFlow.map { it != null }.stateIn(
            coroutineScope,
            started = SharingStarted.Lazily,
            initialValue = settings.selectedEvent != null
        )
    }

    internal val appTheme: StateFlow<AppTheme> by lazy {
        settings.themeFlow
            .stateIn(coroutineScope, started = SharingStarted.Lazily, settings.theme)
    }

    internal fun logout() {
        coroutineScope.launch {
            @Suppress("TooGenericExceptionCaught")
            try {
                val tokens = settings.tokens ?: return@launch
                getKoin().getOrNull<AuthApi>()?.logout(tokens)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                logger.e(e) { "Could not delete session." }
            }
        }

        settings.tokens = null // This also triggers a change to the isLoggedInFlow
        settings.apiBase = null
        settings.selectedEvent = null
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

    const val MIN_UPDATE_INFO_HOURS = 24
}
