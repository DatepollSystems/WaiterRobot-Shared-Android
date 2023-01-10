package org.datepollsystems.waiterrobot.shared.features.table.db

import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import org.datepollsystems.waiterrobot.shared.core.db.AbstractDatabase
import org.datepollsystems.waiterrobot.shared.features.table.db.model.TableEntry
import org.datepollsystems.waiterrobot.shared.utils.extensions.Now
import kotlin.time.Duration

internal class TableDatabase : AbstractDatabase() {

    fun getTablesForEvent(eventId: Long) =
        realm.query<TableEntry>("eventId == $0", eventId).find()

    suspend fun putTables(tables: List<TableEntry>) {
        realm.write {
            tables.forEach { copyToRealm(it, UpdatePolicy.ALL) }
        }
    }

    suspend fun deleteTablesOfEvent(eventId: Long) {
        realm.write {
            delete(query<TableEntry>("eventId == $0", eventId).find())
        }
    }

    suspend fun deleteOlderThan(maxAge: Duration) {
        val timestamp = Now().minus(maxAge).toEpochMilliseconds()
        realm.write {
            delete(query<TableEntry>("updatedAt <= $0", timestamp).find())
        }
    }
}
