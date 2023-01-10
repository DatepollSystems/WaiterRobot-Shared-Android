package org.datepollsystems.waiterrobot.shared.core.db

import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.types.RealmObject
import org.datepollsystems.waiterrobot.shared.features.table.db.model.TableEntry
import kotlin.reflect.KClass

fun createRealmDB(): Realm {
    val schema: Set<KClass<out RealmObject>> = setOf(
        TableEntry::class,
    )

    val config = RealmConfiguration.Builder(schema)
        .deleteRealmIfMigrationNeeded() // Realm is only used as a persistent cache - so do not care about migrations
        .schemaVersion(1) // TODO increase with each version of the common code (automate - compute from version in buildScript or app version?)
        .build()

    return Realm.open(config)
}
