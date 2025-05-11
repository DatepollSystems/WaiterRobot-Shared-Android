package org.datepollsystems.waiterrobot.shared.features.billing.presentation

import dev.icerock.moko.resources.desc.StringDesc
import org.datepollsystems.waiterrobot.shared.core.viewmodel.ViewModelEffect

sealed class BillingEffect : ViewModelEffect {
    data class Toast(val message: StringDesc) : BillingEffect()
    data object ShowPaymentSheet : BillingEffect()
}
