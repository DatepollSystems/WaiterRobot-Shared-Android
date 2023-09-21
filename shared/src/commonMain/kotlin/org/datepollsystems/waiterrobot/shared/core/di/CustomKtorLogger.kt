package org.datepollsystems.waiterrobot.shared.core.di

import org.koin.core.component.KoinComponent
import io.ktor.client.plugins.logging.Logger as KtorLogger

internal class CustomKtorLogger(tagSuffix: String? = null) : KtorLogger, KoinComponent {
    private val logger by injectLogger("Ktor" + tagSuffix?.let { "-$it" }.orEmpty())

    override fun log(message: String) {
        logger.d { message }
    }
}
