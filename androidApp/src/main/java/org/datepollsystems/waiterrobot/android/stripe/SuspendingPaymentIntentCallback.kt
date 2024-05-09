package org.datepollsystems.waiterrobot.android.stripe

import com.stripe.stripeterminal.external.callable.PaymentIntentCallback
import com.stripe.stripeterminal.external.models.PaymentIntent
import com.stripe.stripeterminal.external.models.TerminalException
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class SuspendingPaymentIntentCallback(
    private val continuation: Continuation<PaymentIntent>
) : PaymentIntentCallback {
    override fun onFailure(e: TerminalException) {
        continuation.resumeWithException(e)
    }

    override fun onSuccess(paymentIntent: PaymentIntent) {
        continuation.resume(paymentIntent)
    }
}
