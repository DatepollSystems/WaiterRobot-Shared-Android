package org.datepollsystems.waiterrobot.shared.features.table.db

import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.RealmResults
import org.datepollsystems.waiterrobot.shared.core.db.AbstractDatabase
import org.datepollsystems.waiterrobot.shared.features.table.db.model.TableEntry
import org.datepollsystems.waiterrobot.shared.utils.extensions.Now
import kotlin.time.Duration

internal class TableDatabase : AbstractDatabase() {

    fun getTablesForEvent(eventId: Long): RealmResults<TableEntry> =
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
