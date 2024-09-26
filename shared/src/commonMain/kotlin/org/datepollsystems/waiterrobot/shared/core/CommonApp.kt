package org.datepollsystems.waiterrobot.shared.core

import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerAuthProvider
import io.ktor.client.plugins.pluginOrNull
import io.sentry.kotlin.multiplatform.Sentry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.datepollsystems.waiterrobot.shared.core.data.api.AuthorizedClient
import org.datepollsystems.waiterrobot.shared.core.di.injectLoggerForClass
import org.datepollsystems.waiterrobot.shared.core.navigation.Screen
import org.datepollsystems.waiterrobot.shared.core.settings.SharedSettings
import org.datepollsystems.waiterrobot.shared.features.auth.api.AuthApi
import org.datepollsystems.waiterrobot.shared.features.billing.repository.StripeProvider
import org.datepollsystems.waiterrobot.shared.features.settings.models.AppTheme
import org.datepollsystems.waiterrobot.shared.features.switchevent.models.Event
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

        Sentry.init { options ->
            options.dsn = "https://ae6f703d02014a2ebe206a1d43007ac0@glitchtip.kellner.team/3"
            // options.environment = "production" // TODO
            options.release = appVersion
            options.dist = os.toString()
        }
    }

    internal val isLoggedIn: StateFlow<Boolean> by lazy {
        settings.tokenFlow
            .map { it != null }
            .onEach { logger.d { "Is logged in changed: $it" } }
            .stateIn(coroutineScope, SharingStarted.Eagerly, settings.tokens != null)
    }

    val selectedEvent: StateFlow<Event?> by lazy {
        settings.selectedEventFlow
            .onEach { logger.d { "Selected event changed: $it" } }
            .stateIn(
                coroutineScope,
                started = SharingStarted.Eagerly,
                initialValue = settings.selectedEvent
            )
    }

    internal val appTheme: StateFlow<AppTheme> by lazy {
        settings.themeFlow.stateIn(
            coroutineScope,
            started = SharingStarted.Lazily,
            initialValue = settings.theme
        )
    }

    internal fun logout() {
        val tokens = settings.tokens
        if (tokens != null) {
            coroutineScope.launch {
                @Suppress("TooGenericExceptionCaught")
                try {
                    getKoin().getOrNull<AuthApi>()?.logout(tokens)
                } catch (e: CancellationException) {
                    throw e
                } catch (e: Exception) {
                    logger.e(e) { "Could not delete session." }
                } finally {
                    settings.apiBase = null
                }
            }
        }

        settings.tokens = null // This also triggers a change to the isLoggedInFlow
        settings.selectedEvent = null
        settings.organisationName = ""
        settings.waiterName = ""
        // Reset to default, so that after next login the user will be asked again
        settings.enableContactlessPayment = true

        Sentry.setUser(null)

        // Clear the tokens from the client, so that they get reloaded.
        val apiClients = getKoin().getAll<AuthorizedClient>()
        apiClients.forEach {
            it.delegate.pluginOrNull(Auth)
                ?.providers
                ?.filterIsInstance<BearerAuthProvider>()
                ?.forEach(BearerAuthProvider::clearToken)
        }
    }

    fun getNextRootScreen(): Screen {
        return when {
            settings.tokens == null -> Screen.LoginScreen
            settings.selectedEvent == null -> Screen.SwitchEventScreen
            stripeProvider?.shouldInitializeTerminal() == true -> Screen.StripeInitializationScreen
            else -> Screen.TableListScreen
        }
    }

    const val MIN_UPDATE_INFO_HOURS = 24
}
