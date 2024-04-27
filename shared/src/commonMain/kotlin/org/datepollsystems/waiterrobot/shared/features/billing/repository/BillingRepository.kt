package org.datepollsystems.waiterrobot.shared.features.billing.repository

import org.datepollsystems.waiterrobot.shared.core.repository.AbstractRepository
import org.datepollsystems.waiterrobot.shared.features.billing.api.BillingApi
import org.datepollsystems.waiterrobot.shared.features.billing.api.models.BillResponseDto
import org.datepollsystems.waiterrobot.shared.features.billing.models.BillItem
import org.datepollsystems.waiterrobot.shared.features.table.models.Table
import org.datepollsystems.waiterrobot.shared.utils.cent

internal class BillingRepository(
    private val billingApi: BillingApi,
) : AbstractRepository() {

    suspend fun getBillForTable(table: Table): List<BillItem> {
        return billingApi.getBillForTable(table.id).getBillItems()
    }

    suspend fun payBill(table: Table, items: List<BillItem>): List<BillItem> {
        return billingApi.payBill(
            table.id,
            items.flatMap {
                it.orderProductIds.take(it.selectedForBill)
            }
        ).openBill.getBillItems()
    }

    private fun BillResponseDto.getBillItems(): List<BillItem> {
        return implodedOrderProducts.mapNotNull {
            // Safeguard
            if (it.orderProductIds.isEmpty()) return@mapNotNull null

            BillItem(
                baseProductId = it.baseProductId,
                name = it.name,
                ordered = it.amount,
                selectedForBill = 0,
                pricePerPiece = it.pricePerPiece.cent,
                orderProductIds = it.orderProductIds
            )
        }
    }
}
