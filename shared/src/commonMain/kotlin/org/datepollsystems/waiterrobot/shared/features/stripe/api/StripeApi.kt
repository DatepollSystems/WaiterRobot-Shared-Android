package org.datepollsystems.waiterrobot.shared.features.stripe.api

import io.ktor.client.call.body
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.datepollsystems.waiterrobot.shared.core.CommonApp
import org.datepollsystems.waiterrobot.shared.core.data.remote.AuthorizedApi
import org.datepollsystems.waiterrobot.shared.core.data.remote.AuthorizedClient
import org.datepollsystems.waiterrobot.shared.features.billing.data.remote.dto.BillResponseDto
import org.datepollsystems.waiterrobot.shared.features.billing.data.remote.dto.PayBillRequestDto
import org.datepollsystems.waiterrobot.shared.features.stripe.api.models.PaymentIntent
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

internal class StripeApi(client: AuthorizedClient) : AuthorizedApi("v1/waiter/stripe", client) {
    suspend fun getConnectionToken(eventId: Long): String {
        return get("connectionToken", "eventId" to eventId).bodyAsText()
    }

    suspend fun createPaymentIntent(bill: PayBillRequestDto): PaymentIntent {
        return post("paymentIntent", bill).body<PaymentIntent>()
    }

    suspend fun cancelPaymentIntent(paymentIntent: PaymentIntent): BillResponseDto {
        return post("paymentIntent/${paymentIntent.id}/cancel", body = null).body<BillResponseDto>()
    }
}

class StripeService internal constructor(private val stripeApi: StripeApi) : KoinComponent {
    private val scope: CoroutineScope by inject()

    fun getConnectionToken(callback: (Result<String>) -> Unit) {
        scope.launch {
            val token = runCatching {
                stripeApi.getConnectionToken(eventId = CommonApp.settings.selectedEventId)
            }
            callback(token)
        }
    }
}
