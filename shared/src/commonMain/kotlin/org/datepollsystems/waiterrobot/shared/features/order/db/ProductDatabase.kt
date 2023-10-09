package org.datepollsystems.waiterrobot.shared.features.order.db

import io.realm.kotlin.MutableRealm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.RealmResults
import org.datepollsystems.waiterrobot.shared.core.data.db.AbstractDatabase
import org.datepollsystems.waiterrobot.shared.features.order.db.model.ProductEntry
import org.datepollsystems.waiterrobot.shared.features.order.db.model.ProductGroupEntry
import org.datepollsystems.waiterrobot.shared.utils.extensions.Now
import kotlin.time.Duration

internal class ProductDatabase : AbstractDatabase() {

    fun getForEvent(eventId: Long): RealmResults<ProductGroupEntry> =
        realm.query<ProductGroupEntry>("eventId == $0", eventId).find()

    fun getProductById(id: Long): ProductEntry? =
        realm.query<ProductEntry>("id == $0", id).first().find()

    suspend fun insert(productGroups: List<ProductGroupEntry>) {
        realm.write {
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

    private fun MutableRealm.deleteGroups(groups: RealmResults<ProductGroupEntry>) {
        groups.flatMap(ProductGroupEntry::products).forEach {
            delete(it)
        }
        // TODO how to delete the allergens (they might be referenced by multiple tables...?
        delete(groups)
    }
}
