package org.datepollsystems.waiterrobot.shared.utils.extensions

import kotlinx.coroutines.CancellationException

inline fun <R> runCatchingCancelable(block: () -> R): Result<R> {
    val result = runCatching(block)
    return when (val exception = result.exceptionOrNull()) {
        is CancellationException -> throw exception
        else -> result
    }
}
