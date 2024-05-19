package org.datepollsystems.waiterrobot.shared.core.data.db

import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.types.RealmObject
import org.datepollsystems.waiterrobot.shared.features.order.db.model.AllergenEntry
import org.datepollsystems.waiterrobot.shared.features.order.db.model.ProductEntry
import org.datepollsystems.waiterrobot.shared.features.order.db.model.ProductGroupEntry
import org.datepollsystems.waiterrobot.shared.features.table.db.model.TableEntry
import org.datepollsystems.waiterrobot.shared.features.table.db.model.TableGroupEntry
import kotlin.reflect.KClass

fun createRealmDB(): Realm {
    val schema: Set<KClass<out RealmObject>> = setOf(
        TableGroupEntry::class,
        TableEntry::class,
        ProductGroupEntry::class,
        ProductEntry::class,
        AllergenEntry::class,
    )

    @Suppress("MagicNumber")
    val config = RealmConfiguration.Builder(schema)
        // Realm is only used as a persistent cache - so do not care about migrations
        .deleteRealmIfMigrationNeeded()
        // TODO increase with each version of the common code
        //  (automate - compute from version in buildScript or app version?)
        .schemaVersion(4)
        .build()

    return Realm.open(config)
}
