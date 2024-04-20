package org.datepollsystems.waiterrobot.shared.features.billing.repository

import org.datepollsystems.waiterrobot.shared.core.repository.AbstractRepository
import org.datepollsystems.waiterrobot.shared.features.billing.api.BillingApi
import org.datepollsystems.waiterrobot.shared.features.billing.api.models.PayBillRequestV1Dto
import org.datepollsystems.waiterrobot.shared.features.billing.models.BillItem
import org.datepollsystems.waiterrobot.shared.features.table.models.Table
import org.datepollsystems.waiterrobot.shared.utils.cent

internal class BillingRepository(private val billingApi: BillingApi) : AbstractRepository() {

    suspend fun getBillForTable(table: Table): List<BillItem> {
        return billingApi.getBillForTable(table.id).products.map {
            BillItem(
                productId = it.id,
                name = it.name,
                ordered = it.amount,
                selectedForBill = 0,
                pricePerPiece = it.pricePerPiece.cent
            )
        }
    }

    suspend fun payBill(table: Table, items: List<BillItem>) {
        billingApi.payBill(
            table.id,
            items.mapNotNull {
                if (it.selectedForBill <= 0) {
                    null
                } else {
                    PayBillRequestV1Dto.BillItemDto(it.productId, it.selectedForBill)
                }
            }
        )
    }
}
