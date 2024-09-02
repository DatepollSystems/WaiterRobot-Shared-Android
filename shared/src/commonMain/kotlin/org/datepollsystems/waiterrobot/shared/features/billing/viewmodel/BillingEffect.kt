package org.datepollsystems.waiterrobot.shared.features.billing.viewmodel

import org.datepollsystems.waiterrobot.shared.core.viewmodel.ViewModelEffect

sealed class BillingEffect : ViewModelEffect {
    data class Toast(val message: String) : BillingEffect()
    data object ShowPaymentSheet : BillingEffect()
}
