package org.datepollsystems.waiterrobot.shared.features.billing.repository

import kotlinx.coroutines.flow.StateFlow
import org.datepollsystems.waiterrobot.shared.features.stripe.api.models.PaymentIntent

interface StripeProvider {
    val connectedToReader: StateFlow<Boolean>

    suspend fun initiatePayment(intent: PaymentIntent)

    suspend fun cancelPayment(intent: PaymentIntent)

    fun isGeoLocationEnabled(): Boolean

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
