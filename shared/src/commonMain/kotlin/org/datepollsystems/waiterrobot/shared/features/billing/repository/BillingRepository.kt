package org.datepollsystems.waiterrobot.shared.features.billing.repository

import org.datepollsystems.waiterrobot.shared.core.repository.AbstractRepository
import org.datepollsystems.waiterrobot.shared.features.billing.api.BillingApi
import org.datepollsystems.waiterrobot.shared.features.billing.models.BillItem
import org.datepollsystems.waiterrobot.shared.features.table.models.Table

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
}
