package org.datepollsystems.waiterrobot.shared.utils.extensions

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.time.Duration

internal fun Instant.olderThan(age: Duration) = this < Now().minus(age)

@Suppress("FunctionName")
internal fun Now() = Clock.System.now()
