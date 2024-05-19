package org.datepollsystems.waiterrobot.shared.utils

import kotlinx.coroutines.CancellationException
import org.datepollsystems.waiterrobot.shared.core.CommonApp
import org.datepollsystems.waiterrobot.shared.core.data.api.ApiException
import org.datepollsystems.waiterrobot.shared.core.di.getLogger
import org.datepollsystems.waiterrobot.shared.generated.localization.L
import org.datepollsystems.waiterrobot.shared.generated.localization.accountNotActivated
import org.datepollsystems.waiterrobot.shared.generated.localization.amountToLow
import org.datepollsystems.waiterrobot.shared.generated.localization.desc
import org.datepollsystems.waiterrobot.shared.generated.localization.disabled
import org.datepollsystems.waiterrobot.shared.generated.localization.disabledForEvent
import org.datepollsystems.waiterrobot.shared.generated.localization.generic
import org.datepollsystems.waiterrobot.shared.generated.localization.message
import org.datepollsystems.waiterrobot.shared.generated.localization.title

fun Throwable.getLocalizedUserMessage(): String = when (this) {
    is ApiException -> this.getLocalizedUserMessage()
    is CancellationException -> {
        CommonApp.getLogger("ExceptionMessageMapper").w(this) {
            "Probably caught a CancellationException. CancellationException must not be caught. " +
                "Otherwise structured concurrency does not work correctly."
        }
        L.exceptions.generic()
    }

    else -> L.exceptions.generic()
}

internal fun ApiException.getLocalizedUserMessage(): String = when (this) {
    is ApiException.AccountNotActivated -> L.exceptions.accountNotActivated()
    is ApiException.AppVersionTooOld -> L.app.forceUpdate.message()
    is ApiException.CredentialsIncorrect -> L.root.invalidLoginLink.desc()
    is ApiException.ProductSoldOut -> L.order.productSoldOut.title()
    is ApiException.WaiterCreateTokenIncorrect -> L.login.invalidCode.desc()
    is ApiException.WaiterTokenIncorrect -> L.login.invalidCode.desc()
    is ApiException.BillAmountTooLow -> L.billing.amountToLow(minAmount.cent.toString())
    is ApiException.StripeDisabled -> L.stripeInit.error.disabled()
    is ApiException.StripeNotActivated -> L.stripeInit.error.disabledForEvent()

    // Unknown exceptions or exceptions that should normally not happen
    is ApiException.Generic,
    is ApiException.BadRequest,
    is ApiException.Forbidden,
    is ApiException.NotFound,
    is ApiException.EntityAlreadyExists,
    is ApiException.ServiceUnavailable,
    is ApiException.Unauthorized -> L.exceptions.generic()
}
