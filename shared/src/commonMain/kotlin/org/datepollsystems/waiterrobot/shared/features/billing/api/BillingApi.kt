package org.datepollsystems.waiterrobot.shared.features.billing.api

import io.ktor.client.call.body
import org.datepollsystems.waiterrobot.shared.core.data.api.AuthorizedApi
import org.datepollsystems.waiterrobot.shared.core.data.api.AuthorizedClient
import org.datepollsystems.waiterrobot.shared.features.billing.api.models.BillResponseDto
import org.datepollsystems.waiterrobot.shared.features.billing.api.models.BillResponseDtoV1
import org.datepollsystems.waiterrobot.shared.features.billing.api.models.PayBillRequestDto
import org.datepollsystems.waiterrobot.shared.features.billing.api.models.PayBillRequestV1Dto
import org.datepollsystems.waiterrobot.shared.features.billing.api.models.PayBillResponseDto

internal class BillingApiV1(client: AuthorizedClient) : AuthorizedApi("v1/waiter/billing", client) {

    suspend fun getBillForTable(tableId: Long) = get(tableId.toString()).body<BillResponseDtoV1>()

    suspend fun payBill(
        tableId: Long,
        products: List<PayBillRequestV1Dto.BillItemDto>
    ) = post("pay/$tableId", PayBillRequestV1Dto(tableId, products))
}

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
