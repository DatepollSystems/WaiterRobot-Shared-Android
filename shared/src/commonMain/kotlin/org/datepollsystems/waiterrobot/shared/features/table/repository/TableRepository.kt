package org.datepollsystems.waiterrobot.shared.features.table.repository

import org.datepollsystems.waiterrobot.shared.core.repository.AbstractRepository
import org.datepollsystems.waiterrobot.shared.features.billing.api.BillingApi
import org.datepollsystems.waiterrobot.shared.features.table.api.TableApi
import org.datepollsystems.waiterrobot.shared.features.table.models.OrderedItem
import org.datepollsystems.waiterrobot.shared.features.table.models.Table

internal class TableRepository(private val tableApi: TableApi, private val billingApi: BillingApi) :
    AbstractRepository() {

    suspend fun getTables(forceUpdate: Boolean): List<Table> {
        // TODO get from CacheDB
        return tableApi.getTables().map { Table(it.id, it.number) }
    }

    suspend fun getUnpaidItemsForTable(table: Table): List<OrderedItem> {
        return billingApi.getBillForTable(table.id).products.map {
            OrderedItem(it.id, it.name, it.amount)
        }
    }
}
