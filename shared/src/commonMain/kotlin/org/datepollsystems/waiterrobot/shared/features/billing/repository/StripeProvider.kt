package org.datepollsystems.waiterrobot.shared.features.billing.repository

import org.datepollsystems.waiterrobot.shared.features.stripe.api.models.PaymentIntent

interface StripeProvider {
    suspend fun initiatePayment(intent: PaymentIntent)

    fun isGeoLocationEnabled(): Boolean

    fun isInitialized(): Boolean

    fun initialize()

    suspend fun disconnectReader()

    suspend fun connectLocalReader(locationId: String)
}

abstract class StripeException(message: String, cause: Throwable?) : Exception(message, cause)
class NoReaderFoundException : StripeException("No reader found", cause = null)
class ReaderConnectionFailedException(cause: Throwable?) : StripeException("No reader found", cause)
class ReaderDiscoveryFailedException(cause: Throwable?) : StripeException("No reader found", cause)
