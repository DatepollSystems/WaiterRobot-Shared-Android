package org.datepollsystems.waiterrobot.android.stripe

import com.stripe.stripeterminal.Terminal
import com.stripe.stripeterminal.external.callable.ReaderCallback
import com.stripe.stripeterminal.external.models.ConnectionConfiguration
import com.stripe.stripeterminal.external.models.Reader
import com.stripe.stripeterminal.external.models.TerminalException
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

suspend fun Reader.connect(config: ConnectionConfiguration.LocalMobileConnectionConfiguration): Reader =
    suspendCoroutine {
        Terminal.getInstance().connectLocalMobileReader(this, config, SuspendingConnect(it))
    }

private class SuspendingConnect(
    private val continuation: Continuation<Reader>
) : ReaderCallback {
    override fun onFailure(e: TerminalException) {
        continuation.resumeWithException(e)
    }

    override fun onSuccess(reader: Reader) {
        continuation.resume(reader)
    }
}
