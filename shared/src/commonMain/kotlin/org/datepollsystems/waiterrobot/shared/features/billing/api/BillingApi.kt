package org.datepollsystems.waiterrobot.shared.features.billing.api

import io.ktor.client.call.body
import org.datepollsystems.waiterrobot.shared.core.data.api.AuthorizedApi
import org.datepollsystems.waiterrobot.shared.core.data.api.AuthorizedClient
import org.datepollsystems.waiterrobot.shared.features.billing.api.models.BillResponseDto
import org.datepollsystems.waiterrobot.shared.features.billing.api.models.PayBillRequestV1Dto

internal class BillingApi(client: AuthorizedClient) : AuthorizedApi("waiter/billing", client) {

    suspend fun getBillForTable(tableId: Long) = get(tableId.toString()).body<BillResponseDto>()

    suspend fun payBill(
        tableId: Long,
        products: List<PayBillRequestV1Dto.BillItemDto>
    ) = post("pay/$tableId", PayBillRequestV1Dto(tableId, products))
}
