package org.datepollsystems.waiterrobot.shared.utils

import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.resources.desc.desc
import dev.icerock.moko.resources.format
import kotlinx.coroutines.CancellationException
import org.datepollsystems.waiterrobot.shared.core.CommonApp
import org.datepollsystems.waiterrobot.shared.core.data.remote.ApiException
import org.datepollsystems.waiterrobot.shared.core.di.getLogger
import org.datepollsystems.waiterrobot.shared.localization.MR

fun Throwable.getLocalizedUserMessage(): StringDesc = when (this) {
    is ApiException -> this.getLocalizedUserMessage()
    is CancellationException -> {
        CommonApp.getLogger("ExceptionMessageMapper").w(this) {
            "Probably caught a CancellationException. CancellationException must not be caught. " +
                "Otherwise structured concurrency does not work correctly."
        }
        MR.strings.exceptions_generic.desc()
    }

    else -> MR.strings.exceptions_generic.desc()
}

internal fun ApiException.getLocalizedUserMessage(): StringDesc = when (this) {
    is ApiException.AccountNotActivated -> MR.strings.exceptions_accountNotActivated.desc()
    is ApiException.AppVersionTooOld -> MR.strings.app_forceUpdate_message.desc()
    is ApiException.CredentialsIncorrect -> MR.strings.root_invalidLoginLink_desc.desc()
    is ApiException.ProductSoldOut -> MR.strings.order_product_soldOut_title.desc()
    is ApiException.WaiterCreateTokenIncorrect -> MR.strings.login_scanner_invalidCode_desc.desc()
    is ApiException.WaiterTokenIncorrect -> MR.strings.login_scanner_invalidCode_desc.desc()
    is ApiException.BillAmountTooLow -> MR.strings.billing_amountToLowForMethod.format(minAmount.cent)
    is ApiException.StripeDisabled -> MR.strings.stripeInit_error_disabled.desc()
    is ApiException.StripeNotActivated -> MR.strings.stripeInit_error_disabled_forEvent.desc()
    is ApiException.ProductStockToLow -> MR.strings.order_product_stockToLow_title.desc()
    is ApiException.OrderAlreadySubmitted -> MR.strings.order_alreadyCreated.desc()
    is ApiException.BillProductsAlreadyPayed -> MR.strings.billing_alreadyPaid_desc.desc()
    is ApiException.NoLicence -> MR.strings.exceptions_noLicense.desc()

    // Unknown exceptions or exceptions that should normally not happen
    is ApiException.Generic,
    is ApiException.BadRequest,
    is ApiException.Forbidden,
    is ApiException.NotFound,
    is ApiException.EntityAlreadyExists,
    is ApiException.ServiceUnavailable,
    is ApiException.Unauthorized -> MR.strings.exceptions_generic.desc()
}
