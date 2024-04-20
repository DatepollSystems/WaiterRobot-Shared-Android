package org.datepollsystems.waiterrobot.shared.utils

import kotlinx.serialization.Serializable
import org.datepollsystems.waiterrobot.shared.core.CommonApp
import org.datepollsystems.waiterrobot.shared.utils.extensions.toUrl

sealed class DeepLink {
    sealed class Auth : DeepLink() {
        abstract val apiBase: String

        data class LoginLink(val token: String, override val apiBase: String) : Auth()

        @Serializable // Required for navigation on android
        data class RegisterLink(val token: String, override val apiBase: String) : Auth()
    }

    companion object {
        fun createFromUrl(urlString: String): DeepLink {
            val url = urlString.toUrl()

            require(
                "*" in CommonApp.appInfo.allowedHosts ||
                    url.host in CommonApp.appInfo.allowedHosts
            ) { "Invalid host: ${url.host}" }

            val apiBase = buildString {
                append(url.protocol.name)
                append("://")
                append(url.host)
                append("/api/")
            }

            return when (url.parameters["purpose"]?.lowercase()) {
                "sign_in" -> Auth.LoginLink(url.parameters["token"]!!, apiBase)
                "create" -> Auth.RegisterLink(url.parameters["token"]!!, apiBase)
                else -> throw IllegalArgumentException("Invalid link")
            }
        }
    }
}
