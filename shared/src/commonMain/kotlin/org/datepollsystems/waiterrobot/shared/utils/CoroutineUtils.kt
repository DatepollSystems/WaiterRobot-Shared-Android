package org.datepollsystems.waiterrobot.shared.utils

import co.touchlab.kermit.Logger
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.coroutines.coroutineContext
import kotlin.time.Duration

suspend fun repeatUntilCanceled(
    interval: Duration,
    initialDelay: Duration = Duration.ZERO,
    block: suspend () -> Unit
) {
    delay(initialDelay)
    while (coroutineContext.isActive) {
        block()
        delay(interval)
    }
}

fun CoroutineScope.launchCatching(logger: Logger?, block: suspend () -> Unit) = launch {
    @Suppress("TooGenericExceptionCaught")
    try {
        block()
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        logger?.w(e) { "Encountered exception in launchCatching" }
    }
}
