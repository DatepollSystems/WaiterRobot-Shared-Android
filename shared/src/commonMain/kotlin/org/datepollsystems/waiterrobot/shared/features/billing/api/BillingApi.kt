package org.datepollsystems.waiterrobot.shared.features.billing.api

import io.ktor.client.call.body
import org.datepollsystems.waiterrobot.shared.core.data.api.AuthorizedApi
import org.datepollsystems.waiterrobot.shared.core.data.api.AuthorizedClient
import org.datepollsystems.waiterrobot.shared.features.billing.api.models.BillResponseDto
import org.datepollsystems.waiterrobot.shared.features.billing.api.models.PayBillRequestDto
import org.datepollsystems.waiterrobot.shared.features.billing.api.models.PayBillResponseDto

internal class BillingApi(client: AuthorizedClient) : AuthorizedApi("v2/waiter/billing", client) {

    suspend fun getBillForTable(tableId: Long): BillResponseDto =
        get(tableId.toString()).body<BillResponseDto>()

    suspend fun payBill(
        tableId: Long,
        orderProducts: List<Long>,
        unpaidReasonId: Long? = null
    ): PayBillResponseDto = post(
        endpoint = "pay",
        body = PayBillRequestDto(tableId, orderProducts, unpaidReasonId)
    ).body<PayBillResponseDto>()
}
