package org.datepollsystems.waiterrobot.shared.features.table.db.model

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import kotlinx.datetime.Instant
import org.datepollsystems.waiterrobot.shared.utils.extensions.Now

internal class TableEntry() : RealmObject {
    @PrimaryKey
    var id: Long? = null
    var number: Long? = null
    var eventId: Long? = null
    var groupId: Long? = null
    var groupName: String? = null
    var updatedAt: Long = 0L

    val updated: Instant
        get() = Instant.fromEpochMilliseconds(updatedAt)

    constructor(
        id: Long,
        number: Long,
        eventId: Long,
        groupId: Long,
        groupName: String,
        updatedAt: Instant = Now()
    ) : this() {
        this.id = id
        this.number = number
        this.eventId = eventId
        this.groupId = groupId
        this.groupName = groupName
        this.updatedAt = updatedAt.toEpochMilliseconds()
    }
}
