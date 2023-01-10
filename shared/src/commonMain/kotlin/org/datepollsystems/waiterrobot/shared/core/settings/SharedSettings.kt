package org.datepollsystems.waiterrobot.shared.core.settings

import com.russhwolf.settings.*
import com.russhwolf.settings.coroutines.getLongFlow
import com.russhwolf.settings.coroutines.getStringOrNullFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
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

    // Can not use the settings "native" serialization as this currently can not be combined with settings flow
    internal var tokens: Tokens?
        get() {
            return runCatching {
                settings.getStringOrNull(key = "tokens")?.let { tokenString ->
                    Json.decodeFromString(Tokens.serializer(), tokenString)
                }
            }.getOrNull()
        }
        set(value) {
            val tokenString = runCatching {
                value?.let { Json.encodeToString(Tokens.serializer(), it) }
            }.getOrNull()
            settings.set(key = "tokens", value = tokenString)
        }

    internal val tokenFlow: Flow<Tokens?> =
        settings.getStringOrNullFlow(key = "tokens").map { tokenString ->
            tokenString?.let { it -> Json.decodeFromString(Tokens.serializer(), it) }
        }

    internal var selectedEventId: Long by settings.long("selectedEventId", defaultValue = -1L)
    internal val selectedEventIdFlow: Flow<Long> =
        settings.getLongFlow(key = "selectedEventId", defaultValue = -1)

    internal var appTheme: AppTheme
        get() = AppTheme.fromSettings(settings.getStringOrNull("appTheme"))
        set(value) = settings.set("appTheme", value.name)

    internal val appThemeFlow: Flow<AppTheme> = settings.getStringOrNullFlow("appTheme")
        .map { AppTheme.fromSettings(it) }
}

@Serializable
internal data class Tokens(
    val accessToken: String,
    val refreshToken: String
) {
    companion object {
        fun fromLoginResponse(response: LoginResponseDto): Tokens =
            Tokens(response.accessToken, response.sessionToken)
    }
}
