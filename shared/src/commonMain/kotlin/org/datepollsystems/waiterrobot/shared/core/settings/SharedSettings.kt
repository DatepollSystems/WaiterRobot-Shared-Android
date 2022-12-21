package org.datepollsystems.waiterrobot.shared.core.settings

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.coroutines.getLongFlow
import com.russhwolf.settings.coroutines.getStringOrNullFlow
import com.russhwolf.settings.long
import com.russhwolf.settings.set
import com.russhwolf.settings.string
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.datepollsystems.waiterrobot.shared.features.auth.api.models.LoginResponseDto

@OptIn(ExperimentalSettingsApi::class)
class SharedSettings {
    private val settings by lazy { settingsFactory.create() }

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

    var eventName: String by settings.string(defaultValue = "Unknown")
        internal set
    var organisationName: String by settings.string(defaultValue = "Unknown")
        internal set
    var waiterName: String by settings.string(defaultValue = "Unknown")
        internal set
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
