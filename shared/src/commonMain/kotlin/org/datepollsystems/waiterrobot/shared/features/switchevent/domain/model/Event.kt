package org.datepollsystems.waiterrobot.shared.features.switchevent.domain.model

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
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
    val isDemo: Boolean = false,
) {
    @Serializable
    sealed class StripeSettings {
        @Serializable
        @SerialName("org.datepollsystems.waiterrobot.shared.features.switchevent.models.Event.StripeSettings.Disabled")
        data object Disabled : StripeSettings()

        @Serializable
        @SerialName("org.datepollsystems.waiterrobot.shared.features.switchevent.models.Event.StripeSettings.Enabled")
        data class Enabled(val locationId: String, val minAmount: Int) : StripeSettings()
    }
}
