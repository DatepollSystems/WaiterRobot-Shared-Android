package org.datepollsystems.waiterrobot.shared.utils

import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
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
