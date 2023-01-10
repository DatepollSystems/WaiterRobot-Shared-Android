package org.datepollsystems.waiterrobot.shared.features.order.db

import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.RealmResults
import org.datepollsystems.waiterrobot.shared.core.db.AbstractDatabase
import org.datepollsystems.waiterrobot.shared.features.order.db.model.ProductEntry
import org.datepollsystems.waiterrobot.shared.features.table.db.model.TableEntry
import org.datepollsystems.waiterrobot.shared.utils.extensions.Now
import kotlin.time.Duration

internal class ProductDatabase : AbstractDatabase() {

    fun getForEvent(eventId: Long): RealmResults<ProductEntry> =
        realm.query<ProductEntry>("eventId == $0", eventId).find()

    fun getById(id: Long): ProductEntry? = realm.query<ProductEntry>("id == $0", id).first().find()

    suspend fun insert(products: List<ProductEntry>) {
        realm.write {
            products.forEach { copyToRealm(it, UpdatePolicy.ALL) }
        }
    }

    suspend fun deleteForEvent(eventId: Long) {
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
