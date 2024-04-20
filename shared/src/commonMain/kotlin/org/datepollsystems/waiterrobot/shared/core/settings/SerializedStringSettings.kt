package org.datepollsystems.waiterrobot.shared.core.settings

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.Settings
import com.russhwolf.settings.coroutines.getStringOrNullFlow
import com.russhwolf.settings.set
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

private val settingsJson = Json {
    ignoreUnknownKeys = true
    encodeDefaults = true
    coerceInputValues = true
}

internal inline fun <reified T : Any> Settings.jsonSerialized(
    key: String? = null,
    defaultValue: T
): ReadWriteProperty<Any?, T> = jsonSerialized(key, defaultValue, serializer())

internal fun <T : Any> Settings.jsonSerialized(
    key: String? = null,
    defaultValue: T,
    serializer: KSerializer<T>
): ReadWriteProperty<Any?, T> = JsonSerializedDelegate(this, key, defaultValue, serializer)

internal inline fun <reified T : Any> Settings.nullableJsonSerialized(
    key: String? = null
): ReadWriteProperty<Any?, T?> = nullableJsonSerialized(key, serializer())

internal fun <T : Any> Settings.nullableJsonSerialized(
    key: String? = null,
    serializer: KSerializer<T>
): ReadWriteProperty<Any?, T?> = NullableJsonSerializedDelegate(this, key, serializer)

@OptIn(ExperimentalCoroutinesApi::class, ExperimentalSettingsApi::class)
internal inline fun <reified T : Any> ObservableSettings.jsonSerializedFlow(
    key: String,
    defaultValue: T
): Flow<T> = this.getStringOrNullFlow(key).mapLatest { stringValue ->
    stringValue?.let { settingsJson.decodeFromString(serializer(), it) } ?: defaultValue
}

@OptIn(ExperimentalCoroutinesApi::class, ExperimentalSettingsApi::class)
internal inline fun <reified T : Any> ObservableSettings.jsonSerializedOrNullFlow(
    key: String,
): Flow<T?> = this.getStringOrNullFlow(key).mapLatest { stringValue ->
    stringValue?.let { Json.decodeFromString(serializer(), it) }
}

private class JsonSerializedDelegate<T : Any>(
    private val settings: Settings,
    private val key: String?,
    private val defaultValue: T,
    private val serializer: KSerializer<T>
) : ReadWriteProperty<Any?, T> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return settings.getStringOrNull(key ?: property.name)?.let {
            settingsJson.decodeFromString(serializer, it)
        } ?: defaultValue
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        settings[key ?: property.name] = settingsJson.encodeToString(serializer, value)
    }

    companion object {
        val settingsJson = Json {
            ignoreUnknownKeys = true
        }
    }
}

private class NullableJsonSerializedDelegate<T : Any>(
    private val settings: Settings,
    private val key: String?,
    private val serializer: KSerializer<T>
) : ReadWriteProperty<Any?, T?> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): T? {
        return settings.getStringOrNull(key ?: property.name)?.let {
            JsonSerializedDelegate.settingsJson.decodeFromString(serializer, it)
        }
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
        settings[key ?: property.name] = value?.let {
            JsonSerializedDelegate.settingsJson.encodeToString(serializer, it)
        }
    }
}
