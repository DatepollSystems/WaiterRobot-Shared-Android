package org.datepollsystems.waiterrobot.shared.features.table.repository

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import org.datepollsystems.waiterrobot.shared.core.CommonApp
import org.datepollsystems.waiterrobot.shared.core.repository.AbstractRepository
import org.datepollsystems.waiterrobot.shared.features.billing.api.BillingApi
import org.datepollsystems.waiterrobot.shared.features.table.api.TableApi
import org.datepollsystems.waiterrobot.shared.features.table.api.models.TableResponseDto
import org.datepollsystems.waiterrobot.shared.features.table.db.TableDatabase
import org.datepollsystems.waiterrobot.shared.features.table.db.model.TableEntry
import org.datepollsystems.waiterrobot.shared.features.table.models.OrderedItem
import org.datepollsystems.waiterrobot.shared.features.table.models.Table
import org.datepollsystems.waiterrobot.shared.features.table.models.TableGroup
import org.datepollsystems.waiterrobot.shared.features.table.models.TableGroupWithTables
import org.datepollsystems.waiterrobot.shared.utils.extensions.Now
import org.datepollsystems.waiterrobot.shared.utils.extensions.olderThan
import org.koin.core.component.inject
import kotlin.time.Duration.Companion.hours

internal class TableRepository : AbstractRepository() {
    private val tableApi: TableApi by inject()
    private val billingApi: BillingApi by inject()
    private val tableDb: TableDatabase by inject()
    private val coroutineScope: CoroutineScope by inject()

    init {
        // Delete outdated at app start
        coroutineScope.launch {
            tableDb.deleteOlderThan(maxAge)
        }
    }

    suspend fun getTableGroups(forceUpdate: Boolean): List<TableGroupWithTables> {
        val eventId = CommonApp.settings.selectedEventId

        fun loadFromDb(): List<TableGroupWithTables>? {
            logger.i { "Fetching tables from DB ..." }
            val dbTables = tableDb.getTablesForEvent(eventId)
            logger.d { "Found ${dbTables.count()} tables in DB" }

            return if (dbTables.isEmpty() || dbTables.any { it.updated.olderThan(maxAge) }) {
                null
            } else {
                return dbTables.groupBy { TableGroup(it.groupId!!, it.groupName!!) }
                    .map { (group, tables) ->
                        TableGroupWithTables(group, tables.map(TableEntry::toModel))
                    }
            }
        }

        suspend fun loadFromApiAndStore(): List<TableGroupWithTables> {
            logger.i { "Loading Tables from api ..." }

            val timestamp = Now()
            val apiTables = tableApi.getTables()
            logger.d { "Got ${apiTables.count()} tables from api" }

            logger.d { "Remove old tables from DB ..." }
            tableDb.deleteTablesOfEvent(eventId)

            logger.d { "Saving tables to DB ..." }
            tableDb.putTables(apiTables.map { it.toEntry(timestamp) })

            return apiTables.groupBy { TableGroup(it.groupId, it.groupName) }
                .map { (group, tables) ->
                    TableGroupWithTables(group, tables.map(TableResponseDto::toModel))
                }
        }

        return if (forceUpdate) {
            loadFromApiAndStore()
        } else {
            loadFromDb() ?: loadFromApiAndStore()
        }
    }

    suspend fun getTables(forceUpdate: Boolean): List<Table> {
        return emptyList()
    }

    suspend fun getUnpaidItemsForTable(table: Table): List<OrderedItem> {
        return billingApi.getBillForTable(table.id).products.map {
            OrderedItem(it.id, it.name, it.amount)
        }
    }

    companion object {
        private val maxAge = 24.hours
    }
}

private fun TableResponseDto.toModel() = Table(
    id = this.id,
    number = this.number,
)

private fun TableEntry.toModel() = Table(
    id = this.id!!,
    number = this.number!!,
)

private fun TableResponseDto.toEntry(timestamp: Instant) = TableEntry(
    id = this.id,
    number = this.number,
    eventId = this.eventId,
    groupId = this.groupId,
    groupName = this.groupName,
    updatedAt = timestamp
)
