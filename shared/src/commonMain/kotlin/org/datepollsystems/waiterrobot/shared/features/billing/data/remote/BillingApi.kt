package org.datepollsystems.waiterrobot.shared.features.billing.data.remote

import io.ktor.client.call.body
import org.datepollsystems.waiterrobot.shared.core.data.remote.AuthorizedApi
import org.datepollsystems.waiterrobot.shared.core.data.remote.AuthorizedClient
import org.datepollsystems.waiterrobot.shared.features.billing.data.remote.dto.BillResponseDto
import org.datepollsystems.waiterrobot.shared.features.billing.data.remote.dto.PayBillRequestDto
import org.datepollsystems.waiterrobot.shared.features.billing.data.remote.dto.PayBillResponseDto

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
