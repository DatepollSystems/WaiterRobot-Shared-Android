package org.datepollsystems.waiterrobot.shared.features.switchevent.models

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class Event(
    val id: Long,
    val name: String,
    val startDate: Instant?,
    val endDate: Instant?,
    val city: String,
    val organisationId: Long,
    val stripeSettings: StripeSettings,
) {
    @Serializable
    sealed interface StripeSettings {
        @Serializable
        data object Disabled : StripeSettings

        @Serializable
        data class Enabled(val locationId: String, val minAmount: Int) : StripeSettings
    }
}
