package org.datepollsystems.waiterrobot.shared.features.stripe.api.models

import kotlinx.serialization.Serializable

@Serializable
data class PaymentIntent(
    val id: String,
    val clientSecret: String
)
