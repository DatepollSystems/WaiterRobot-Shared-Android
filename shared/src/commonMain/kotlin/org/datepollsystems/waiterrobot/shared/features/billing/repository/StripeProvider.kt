package org.datepollsystems.waiterrobot.shared.features.billing.repository

import org.datepollsystems.waiterrobot.shared.features.stripe.api.models.PaymentIntent

interface StripeProvider {
    suspend fun initiatePayment(intent: PaymentIntent)

    fun isGeoLocationEnabled(): Boolean

    fun isInitialized(): Boolean
}
