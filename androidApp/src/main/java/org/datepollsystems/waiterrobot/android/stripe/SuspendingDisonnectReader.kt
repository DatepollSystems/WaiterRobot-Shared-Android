package org.datepollsystems.waiterrobot.android.stripe

import com.stripe.stripeterminal.Terminal
import com.stripe.stripeterminal.external.callable.Callback
import com.stripe.stripeterminal.external.models.TerminalException
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

suspend fun Terminal.Companion.disconnectReader() = suspendCoroutine {
    getInstance().disconnectReader(SuspendingDisconnect(it))
}

private class SuspendingDisconnect(
    private val continuation: Continuation<Unit>
) : Callback {
    override fun onFailure(e: TerminalException) {
        continuation.resumeWithException(e)
    }

    override fun onSuccess() {
        continuation.resume(Unit)
    }
}
