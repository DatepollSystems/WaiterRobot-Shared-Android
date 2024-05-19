package org.datepollsystems.waiterrobot.shared.features.table.db.model

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

internal class TableEntry() : RealmObject {
    @PrimaryKey
    var id: Long = -1
    var number: Int = -1
    var hasOrders: Boolean = false

    constructor(
        id: Long,
        number: Int,
    ) : this() {
        this.id = id
        this.number = number
    }
}
