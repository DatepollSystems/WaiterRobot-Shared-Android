package org.datepollsystems.waiterrobot.shared.core

import co.touchlab.kermit.Logger
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.takeFrom
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.until
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.datepollsystems.waiterrobot.shared.core.CommonApp.MIN_UPDATE_INFO_HOURS
import org.datepollsystems.waiterrobot.shared.utils.extensions.defaultOnNull
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.parameter.parametersOf

@Suppress("unused")
object VersionChecker : KoinComponent {
    var storeUrl: String? = null

    fun checkVersion(onNewVersionAvailable: () -> Unit) {
        val client: HttpClient = get()
        val scope: CoroutineScope = get()
        val logger: Logger = get { parametersOf(this::class.simpleName!!) }
        val json: Json = get()

        scope.launch {
            runCatching {
                // API does return text/javascript as content type -> manual deserialization
                val detailsString = client.get {
                    url {
                        takeFrom("https://itunes.apple.com/at/lookup")
                        this.parameters.append("bundleId", "org.datepollsystems.waiterrobot")
                    }
                }.bodyAsText()

                val details = json.decodeFromString<AppDetails>(detailsString)
                val newest = details.results
                    .associateBy { Version.fromString(it.version) }
                    .maxBy { it.key }
                val installed = Version.fromString(
                    CommonApp.appInfo.appVersion.substringBefore("-")
                )

                storeUrl = newest.value.trackViewUrl

                logger.d("Newest available version: ${newest.key}, Installed version: $installed")

                if (newest.key > installed) {
                    val hoursSinceLastUpdateAvailableNote =
                        CommonApp.settings.lastUpdateAvailableNote
                            .defaultOnNull(Instant.DISTANT_PAST)
                            .until(Clock.System.now(), DateTimeUnit.HOUR)

                    logger.i(
                        "New app version is available, hoursSinceLastUpdateAvailableNote: " +
                            hoursSinceLastUpdateAvailableNote
                    )

                    // Show max once a day
                    if (hoursSinceLastUpdateAvailableNote > MIN_UPDATE_INFO_HOURS) {
                        onNewVersionAvailable()
                        CommonApp.settings.lastUpdateAvailableNote = Clock.System.now()
                    }
                } else {
                    logger.i("No new app version available")
                }
            }.onFailure {
                logger.w("checkVersion for iOS failed", it)
            }
        }
    }

    @Serializable
    private class AppDetails(
        val results: List<AppDetail>
    ) {
        @Serializable
        class AppDetail(
            val version: String,
            val trackViewUrl: String
        )
    }

    private data class Version(
        val major: Int,
        val minor: Int,
        val patch: Int
    ) : Comparable<Version> {
        override operator fun compareTo(other: Version): Int {
            return when {
                this.major != other.major -> this.major.compareTo(other.major)
                this.minor != other.minor -> this.minor.compareTo(other.minor)
                this.patch != other.patch -> this.patch.compareTo(other.patch)
                else -> 0
            }
        }

        companion object {
            fun fromString(versionString: String): Version =
                versionString.split(".").let { (major, minor, patch) ->
                    Version(major.toInt(), minor.toInt(), patch.toInt())
                }
        }
    }
}
