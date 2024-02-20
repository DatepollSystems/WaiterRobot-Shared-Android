package org.datepollsystems.waiterrobot.shared.features.order.db

import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.datepollsystems.waiterrobot.shared.core.data.db.AbstractDatabase
import org.datepollsystems.waiterrobot.shared.features.order.db.model.ProductEntry
import org.datepollsystems.waiterrobot.shared.features.order.db.model.ProductGroupEntry
import org.datepollsystems.waiterrobot.shared.features.table.db.model.TableGroupEntry
import org.datepollsystems.waiterrobot.shared.utils.extensions.Now
import kotlin.time.Duration

internal class ProductDatabase : AbstractDatabase() {

    fun getForEventFlow(eventId: Long): Flow<List<ProductGroupEntry>> =
        realm.query<ProductGroupEntry>("eventId == $0", eventId).asFlow().map { it.list }

    suspend fun getProductById(id: Long): ProductEntry? =
        realm.query<ProductEntry>("id == $0", id).first().asFlow().map { it.obj }.first()

    suspend fun replace(productGroups: List<ProductGroupEntry>) {
        val idsToKeep = productGroups.mapTo(mutableSetOf(), ProductGroupEntry::id)

        realm.write {
            delete(query<TableGroupEntry>("id == NONE $0", idsToKeep))
            productGroups.forEach { copyToRealm(it, UpdatePolicy.ALL) }
        }
    }

    suspend fun deleteForEvent(eventId: Long) {
        realm.write {
            delete(query<ProductGroupEntry>("eventId == $0", eventId).find())
        }
    }

    suspend fun deleteOlderThan(maxAge: Duration) {
        val timestamp = Now().minus(maxAge).toEpochMilliseconds()
        realm.write {
            delete(query<ProductGroupEntry>("updatedAt <= $0", timestamp).find())
        }
    }
}
