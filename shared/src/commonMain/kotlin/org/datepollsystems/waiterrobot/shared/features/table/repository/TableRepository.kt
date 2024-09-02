package org.datepollsystems.waiterrobot.shared.features.table.repository

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Instant
import org.datepollsystems.waiterrobot.shared.core.CommonApp
import org.datepollsystems.waiterrobot.shared.core.data.Resource
import org.datepollsystems.waiterrobot.shared.core.repository.CachedRepository
import org.datepollsystems.waiterrobot.shared.features.billing.repository.BillingRepository
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
import kotlin.time.Duration.Companion.hours

internal class TableRepository(
    private val tableApi: TableApi,
    private val billingRepository: BillingRepository,
    private val tableDb: TableDatabase,
) : CachedRepository<List<TableGroupEntry>, List<TableGroup>>() {

    override suspend fun onStart() {
        tableDb.deleteOlderThan(maxAge)
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
            billingRepository.getBillForTable(table, selectAll = false).map {
                OrderedItem(it.baseProductId, it.name, it.ordered, it.virtualId)
            }
        }

    override fun query(): Flow<List<TableGroupEntry>> =
        tableDb.getForEventFlow(CommonApp.settings.selectedEventId).map { groups ->
            groups.filter { it.tables.isNotEmpty() } // Do not show groups that do not have tables at all
                .sortedBy { it.name.lowercase() } // Sort groups with same position by name
                .sortedBy(TableGroupEntry::position)
        }

    override suspend fun update() {
        logger.i { "Loading Tables from api ..." }

        val timestamp = Now()
        val apiTables = tableApi.getTableGroups()
        logger.d { "Got ${apiTables.count()} table groups with ${apiTables.sumOf { it.tables.count() }} from api" }

        logger.d { "Update tables in DB ..." }
        val tableIdsWithOrders = tableApi.getTableIdsOfTablesWithOpenOrder()
        tableDb.replace(apiTables.map { it.toEntry(timestamp) }, tableIdsWithOrders)
    }

    override fun mapDbEntity(dbEntity: List<TableGroupEntry>): List<TableGroup> =
        dbEntity.map(TableGroupEntry::toModel)

    override fun shouldFetch(cache: List<TableGroupEntry>): Boolean {
        return cache.isEmpty() ||
            cache.any { it.updated.olderThan(maxAge) }
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
