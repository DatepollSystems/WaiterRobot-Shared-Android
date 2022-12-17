package org.datepollsystems.waiterrobot.shared.utils

import org.datepollsystems.waiterrobot.shared.utils.extensions.toUrl

sealed class DeepLink {
    sealed class Auth : DeepLink() {
        data class LoginLink(val token: String) : Auth()
        data class RegisterLink(val token: String) : Auth()
    }

    companion object {
        fun createFromUrl(urlString: String): DeepLink {
            val url = urlString.toUrl()

            return when (url.parameters["purpose"]?.lowercase()) {
                "sign_in" -> Auth.LoginLink(url.parameters["token"]!!)
                "create" -> Auth.RegisterLink(url.parameters["token"]!!)
                else -> throw IllegalArgumentException("Invalid link")
            }
        }
    }
}
