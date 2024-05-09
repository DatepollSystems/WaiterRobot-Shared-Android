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

    suspend fun initiatePayment(intent: PaymentIntent)

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

abstract class StripeException(message: String, cause: Throwable?) : Exception(message, cause)
class NoReaderFoundException : StripeException("No reader found", cause = null)
class ReaderConnectionFailedException(cause: Throwable?) : StripeException("No reader found", cause)
class ReaderDiscoveryFailedException(cause: Throwable?) : StripeException("No reader found", cause)
class TerminalInitializationFailedException(cause: Throwable?) :
    StripeException("Failed to initialize terminal", cause)
