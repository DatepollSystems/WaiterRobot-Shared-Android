package org.datepollsystems.waiterrobot.shared.features.billing.api

import io.ktor.client.*
import io.ktor.client.call.*
import org.datepollsystems.waiterrobot.shared.core.api.AbstractApi
import org.datepollsystems.waiterrobot.shared.features.billing.api.models.BillResponseDto
import org.datepollsystems.waiterrobot.shared.features.billing.api.models.PayBillRequestDto

internal class BillingApi(client: HttpClient) : AbstractApi("waiter/billing", client) {

    suspend fun getBillForTable(tableId: Long) = get(tableId.toString()).body<BillResponseDto>()

    suspend fun payBill(
        tableId: Long,
        products: List<PayBillRequestDto.BillItemDto>
    ) = post("pay/$tableId", PayBillRequestDto(products))
}
