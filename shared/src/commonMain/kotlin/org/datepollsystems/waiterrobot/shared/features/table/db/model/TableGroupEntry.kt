package org.datepollsystems.waiterrobot.shared.features.table.db.model

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.ext.toRealmList
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import kotlinx.datetime.Instant
import org.datepollsystems.waiterrobot.shared.utils.extensions.Now

internal class TableGroupEntry() : RealmObject {
    @PrimaryKey
    var id: Long = -1
    var name: String = ""
    var eventId: Long = -1
    var position: Int = Int.MAX_VALUE
    var color: String? = null
    var isFiltered: Boolean = false
    var tables: RealmList<TableEntry> = realmListOf()
    var updatedAt: Long = 0L

    val updated: Instant
        get() = Instant.fromEpochMilliseconds(updatedAt)

    constructor(
        id: Long,
        name: String,
        eventId: Long,
        position: Int,
        color: String?,
        tables: List<TableEntry>,
        updatedAt: Instant = Now()
    ) : this() {
        this.id = id
        this.name = name
        this.eventId = eventId
        this.position = position
        this.color = color
        this.tables = tables.toRealmList()
        this.updatedAt = updatedAt.toEpochMilliseconds()
    }
}
