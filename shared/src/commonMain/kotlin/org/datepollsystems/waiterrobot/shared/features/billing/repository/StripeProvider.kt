package org.datepollsystems.waiterrobot.shared.features.billing.repository

import kotlinx.coroutines.flow.StateFlow
import org.datepollsystems.waiterrobot.shared.core.CommonApp
import org.datepollsystems.waiterrobot.shared.features.stripe.api.models.PaymentIntent
import org.datepollsystems.waiterrobot.shared.features.switchevent.models.Event

interface StripeProvider {
    val connectedToReader: StateFlow<Boolean>

    fun shouldInitializeTerminal(): Boolean =
        CommonApp.settings.enableContactlessPayment &&
            CommonApp.settings.selectedEvent?.stripeSettings is Event.StripeSettings.Enabled &&
            !connectedToReader.value

    suspend fun collectPayment(intent: PaymentIntent): Boolean

    suspend fun cancelPayment(intent: PaymentIntent)

    // TODO move to location provider
    fun isGeoLocationEnabled(): Boolean

    // TODO move to NFC provider
    fun isNfcEnabled(): Boolean

    fun isInitialized(): Boolean

    fun initialize()

    suspend fun disconnectReader()

    suspend fun connectLocalReader(locationId: String)
}

open class StripeException(message: String, cause: Throwable?) : Exception(message, cause) {
    constructor(
        message: String,
        stripeErrorCode: String,
        cause: Throwable?,
    ) : this("$message (code=$stripeErrorCode)", cause)
}

class NoReaderFoundException : StripeException("No reader found", cause = null)

class PaymentCanceledException(cause: Throwable?, stripeErrorCode: String) :
    StripeException("Payment was canceled", stripeErrorCode, cause)

class GeoLocationDisabledException(cause: Throwable?, stripeErrorCode: String) :
    StripeException("Geo location is disabled", stripeErrorCode, cause)

class NfcDisabledException(cause: Throwable?, stripeErrorCode: String) :
    StripeException("NFC is disabled", stripeErrorCode, cause)
