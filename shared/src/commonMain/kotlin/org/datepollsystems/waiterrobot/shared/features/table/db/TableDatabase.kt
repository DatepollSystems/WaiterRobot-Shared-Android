package org.datepollsystems.waiterrobot.shared.features.table.db

import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.find
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.datepollsystems.waiterrobot.shared.core.data.db.AbstractDatabase
import org.datepollsystems.waiterrobot.shared.features.table.db.model.TableEntry
import org.datepollsystems.waiterrobot.shared.features.table.db.model.TableGroupEntry
import org.datepollsystems.waiterrobot.shared.utils.extensions.Now
import kotlin.time.Duration

internal class TableDatabase : AbstractDatabase() {

    fun getForEventFlow(eventId: Long): Flow<List<TableGroupEntry>> =
        realm.query<TableGroupEntry>("eventId == $0", eventId).asFlow().map { it.list }

    suspend fun replace(tableGroups: List<TableGroupEntry>, tableIdsWithOrders: Set<Long>) {
        val idsToKeep = tableGroups.mapTo(mutableSetOf(), TableGroupEntry::id)
        // Realm does currently not have the concept of partial update.
        // Always the whole object is overridden.
        val hiddenGroupIds = realm.query<TableGroupEntry>("hidden == $0", true)
            .find { it.mapTo(mutableSetOf(), TableGroupEntry::id) }
        tableGroups.forEach {
            it.hidden = it.id in hiddenGroupIds
            it.tables.forEach { table ->
                table.hasOrders = table.id in tableIdsWithOrders
            }
        }

        realm.write {
            delete(query<TableGroupEntry>("id == NONE $0", idsToKeep))
            tableGroups.forEach { copyToRealm(it, UpdatePolicy.ALL) }
        }
    }

    suspend fun updateTablesWithOrder(tableIdsWithOrders: Set<Long>) {
        val tables = realm.query<TableEntry>().find()
        realm.write {
            tables.forEach { table ->
                findLatest(table)?.let {
                    it.hasOrders = it.id in tableIdsWithOrders
                }
            }
        }
    }

    suspend fun toggleHidden(id: Long) {
        realm.write {
            val group = query<TableGroupEntry>("id == $0", id).first().find() ?: return@write
            group.hidden = !group.hidden
        }
    }

    suspend fun deleteOlderThan(maxAge: Duration) {
        val timestamp = Now().minus(maxAge).toEpochMilliseconds()
        realm.write {
            delete(query<TableGroupEntry>("updatedAt <= $0", timestamp).find())
        }
    }

    suspend fun showAll() {
        val groups = realm.query<TableGroupEntry>().find()
        realm.write {
            groups.forEach {
                findLatest(it)?.hidden = false
            }
        }
    }

    suspend fun hideAll() {
        val groups = realm.query<TableGroupEntry>().find()
        realm.write {
            groups.forEach {
                findLatest(it)?.hidden = true
            }
        }
    }
}
