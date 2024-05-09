package org.datepollsystems.waiterrobot.android.stripe

import com.stripe.stripeterminal.Terminal
import com.stripe.stripeterminal.external.callable.Callback
import com.stripe.stripeterminal.external.models.CollectConfiguration
import com.stripe.stripeterminal.external.models.PaymentIntent
import com.stripe.stripeterminal.external.models.TerminalException
import kotlinx.coroutines.suspendCancellableCoroutine
import org.datepollsystems.waiterrobot.shared.core.di.injectLoggerForClass
import org.koin.core.component.KoinComponent
import kotlin.coroutines.suspendCoroutine

suspend fun Terminal.Companion.retrievePaymentIntent(clientSecret: String) = suspendCoroutine {
    getInstance().retrievePaymentIntent(clientSecret, SuspendingPaymentIntentCallback(it))
}

suspend fun PaymentIntent.collectPaymentMethod(
    config: CollectConfiguration = CollectConfiguration.Builder().build()
) = suspendCancellableCoroutine {
    val cancelable = Terminal.getInstance()
        .collectPaymentMethod(this, SuspendingPaymentIntentCallback(it), config)
    it.invokeOnCancellation { cancelable.cancel(NoopCallback("Cancel collectPayment")) }
}

suspend fun PaymentIntent.confirm() = suspendCoroutine {
    Terminal.getInstance().confirmPaymentIntent(this, SuspendingPaymentIntentCallback(it))
}

suspend fun PaymentIntent.cancel() = suspendCoroutine {
    Terminal.getInstance().cancelPaymentIntent(this, SuspendingPaymentIntentCallback(it))
}

class NoopCallback(private val identifier: String) : Callback, KoinComponent {
    private val logger by injectLoggerForClass()

    override fun onFailure(e: TerminalException) {
        // Ignore
        logger.e("Failed for $identifier", e)
    }

    override fun onSuccess() {
        // Ignore
        logger.d("Success for $identifier")
    }
}
