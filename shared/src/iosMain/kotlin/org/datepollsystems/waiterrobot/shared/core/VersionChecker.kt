package org.datepollsystems.waiterrobot.shared.core

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.takeFrom
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.until
import kotlinx.serialization.Serializable
import org.datepollsystems.waiterrobot.shared.utils.extensions.defaultOnNull
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

object VersionChecker : KoinComponent {
    var storeUrl: String? = null

    fun checkVersion(onNewVersionAvailable: () -> Unit) {
        val client: HttpClient = get()
        val scope: CoroutineScope = get()

        scope.launch {
            runCatching {
                val details = client.get {
                    url {
                        takeFrom("http://itunes.apple.com/at/lookup")
                        this.parameters.append("bundleId", "org.datepollsystems.waiterrobot")
                    }
                }.body<AppDetails>()

                val newest = details.results
                    .associateBy { Version.fromString(it.version) }
                    .maxBy { it.key }
                val installed = Version.fromString(
                    CommonApp.appInfo.appVersion.substringBefore("-")
                )

                storeUrl = newest.value.trackViewUrl

                if (newest.key > installed
                    && CommonApp.settings.lastUpdateAvailableNote // Show max once a day
                        .defaultOnNull(Instant.DISTANT_PAST)
                        .until(Clock.System.now(), DateTimeUnit.HOUR) > 24
                ) {
                    onNewVersionAvailable()
                    CommonApp.settings.lastUpdateAvailableNote = Clock.System.now()
                }
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
