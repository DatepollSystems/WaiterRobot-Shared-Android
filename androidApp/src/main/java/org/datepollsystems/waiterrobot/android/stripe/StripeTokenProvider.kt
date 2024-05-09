package org.datepollsystems.waiterrobot.android.stripe

import com.stripe.stripeterminal.external.callable.ConnectionTokenCallback
import com.stripe.stripeterminal.external.callable.ConnectionTokenProvider
import com.stripe.stripeterminal.external.models.ConnectionTokenException
import org.datepollsystems.waiterrobot.shared.features.stripe.api.StripeService

class StripeTokenProvider(private val stripeService: StripeService) : ConnectionTokenProvider {
    override fun fetchConnectionToken(callback: ConnectionTokenCallback) {
        stripeService.getConnectionToken { result ->
            result.fold(
                onSuccess = { callback.onSuccess(it) },
                onFailure = {
                    callback.onFailure(
                        ConnectionTokenException("Failed to fetch connection token", it)
                    )
                }
            )
        }
    }
}
