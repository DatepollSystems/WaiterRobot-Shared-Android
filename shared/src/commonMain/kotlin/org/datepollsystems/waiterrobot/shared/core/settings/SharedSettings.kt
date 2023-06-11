package org.datepollsystems.waiterrobot.shared.core.settings

import com.russhwolf.settings.*
import com.russhwolf.settings.coroutines.getLongFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import org.datepollsystems.waiterrobot.shared.features.auth.api.models.LoginResponseDto
import org.datepollsystems.waiterrobot.shared.features.settings.models.AppTheme
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@OptIn(ExperimentalSettingsApi::class)
class SharedSettings : KoinComponent {
    private val settings: ObservableSettings by inject()

    var eventName: String by settings.string(defaultValue = "Unknown")
        internal set
    var organisationName: String by settings.string(defaultValue = "Unknown")
        internal set
    var waiterName: String by settings.string(defaultValue = "Unknown")
        internal set
    var lastUpdateAvailableNote: Instant? by settings.nullableJsonSerialized()

    // Can not use the settings "native" serialization as this currently can not be combined with settings flow
    internal var tokens: Tokens? by settings.nullableJsonSerialized()
    internal val tokenFlow: Flow<Tokens?> =
        settings.jsonSerializedOrNullFlow(SharedSettings::tokens.name)

    internal var selectedEventId: Long by settings.long("selectedEventId", defaultValue = -1L)
    internal val selectedEventIdFlow: Flow<Long> =
        settings.getLongFlow(key = "selectedEventId", defaultValue = -1)

    internal var appTheme: AppTheme by settings.jsonSerialized(defaultValue = AppTheme.SYSTEM)
    internal val appThemeFlow: Flow<AppTheme> =
        settings.jsonSerializedFlow(SharedSettings::appTheme.name, AppTheme.SYSTEM)
}

@Serializable
internal data class Tokens(
    val accessToken: String,
    val refreshToken: String
) {
    companion object {
        fun fromLoginResponse(response: LoginResponseDto): Tokens =
            Tokens(response.accessToken, response.refreshToken)
    }
}
