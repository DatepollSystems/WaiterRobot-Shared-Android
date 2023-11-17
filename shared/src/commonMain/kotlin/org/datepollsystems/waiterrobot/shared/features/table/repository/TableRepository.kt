package org.datepollsystems.waiterrobot.shared.features.table.repository

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import org.datepollsystems.waiterrobot.shared.core.CommonApp
import org.datepollsystems.waiterrobot.shared.core.data.Resource
import org.datepollsystems.waiterrobot.shared.core.data.cachedRemoteResource
import org.datepollsystems.waiterrobot.shared.core.data.mapResource
import org.datepollsystems.waiterrobot.shared.core.data.remoteResource
import org.datepollsystems.waiterrobot.shared.core.repository.AbstractRepository
import org.datepollsystems.waiterrobot.shared.features.billing.api.BillingApi
import org.datepollsystems.waiterrobot.shared.features.table.api.TableApi
import org.datepollsystems.waiterrobot.shared.features.table.api.models.TableGroupResponseDto
import org.datepollsystems.waiterrobot.shared.features.table.api.models.TableResponseDto
import org.datepollsystems.waiterrobot.shared.features.table.db.TableDatabase
import org.datepollsystems.waiterrobot.shared.features.table.db.model.TableEntry
import org.datepollsystems.waiterrobot.shared.features.table.db.model.TableGroupEntry
import org.datepollsystems.waiterrobot.shared.features.table.models.OrderedItem
import org.datepollsystems.waiterrobot.shared.features.table.models.Table
import org.datepollsystems.waiterrobot.shared.features.table.models.TableGroup
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

    fun getTableGroupsFlow(forceUpdate: Boolean): Flow<Resource<List<TableGroup>>> =
        cachedRemoteResource(
            query = { tableDb.getForEventFlow(CommonApp.settings.selectedEventId) },
            update = ::refreshTableGroups,
            shouldFetch = { cachedGroups ->
                forceUpdate || cachedGroups.isEmpty() ||
                    cachedGroups.any { it.updated.olderThan(maxAge) }
            },
            mapDbEntity = { it.map(TableGroupEntry::toModel) }
        ).mapResource { groups ->
            groups?.filter { it.tables.isNotEmpty() } // Do not show groups that do not have tables at all
                ?.sortedBy { it.name.lowercase() } // Sort groups with same position by name
                ?.sortedBy(TableGroup::position)
        }

    suspend fun refreshTableGroups() {
        logger.i { "Loading Tables from api ..." }

        val timestamp = Now()
        val apiTables = tableApi.getTableGroups()
        logger.d { "Got ${apiTables.count()} table groups with ${apiTables.sumOf { it.tables.count() }} from api" }

        logger.d { "Update tables in DB ..." }
        val tableIdsWithOrders = tableApi.getTableIdsOfTablesWithOpenOrder()
        tableDb.replace(apiTables.map { it.toEntry(timestamp) }, tableIdsWithOrders)
    }

    suspend fun updateTablesWithOpenOrder() {
        @Suppress("TooGenericExceptionCaught")
        try {
            val ids = tableApi.getTableIdsOfTablesWithOpenOrder()
            tableDb.updateTablesWithOrder(ids)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            logger.i("Refreshing of tables with open orders failed", e)
        }
    }

    suspend fun toggleGroupFilter(group: TableGroup) {
        tableDb.toggleHidden(group.id)
    }

    suspend fun showAll() {
        tableDb.showAll()
    }

    suspend fun hideAll() {
        tableDb.hideAll()
    }

    suspend fun getUnpaidItemsForTable(table: Table): Flow<Resource<List<OrderedItem>>> =
        remoteResource {
            billingApi.getBillForTable(table.id).products.map {
                OrderedItem(it.id, it.name, it.amount)
            }
        }

    companion object {
        private val maxAge = 24.hours
    }
}

private fun TableEntry.toModel(groupName: String) = Table(
    id = this.id,
    number = this.number,
    hasOrders = this.hasOrders,
    groupName = groupName,
)

private fun TableGroupEntry.toModel() = TableGroup(
    id = this.id,
    name = this.name,
    eventId = this.eventId,
    position = this.position,
    color = this.color,
    hidden = this.hidden,
    tables = this.tables.map { it.toModel(this.name) }.sort()
)

private fun TableGroupResponseDto.toEntry(timestamp: Instant) = TableGroupEntry(
    id = this.id,
    name = this.name,
    eventId = this.eventId,
    position = this.position,
    color = this.color,
    tables = this.tables.map(TableResponseDto::toEntry),
    updatedAt = timestamp
)

private fun TableResponseDto.toEntry() = TableEntry(
    id = this.id,
    number = this.number,
)

private fun List<Table>.sort() = this.sortedBy(Table::number)
