package org.datepollsystems.waiterrobot.shared.features.order.db.model

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.ext.toRealmList
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import kotlinx.datetime.Instant
import org.datepollsystems.waiterrobot.shared.utils.Cents
import org.datepollsystems.waiterrobot.shared.utils.extensions.Now

internal class ProductGroupEntry() : RealmObject {
    @PrimaryKey
    var id: Long = -1
    var name: String = ""
    var eventId: Long = -1
    var position: Int = Int.MAX_VALUE
    var products: RealmList<ProductEntry> = realmListOf()
    var color: String? = null
    var updatedAt: Long = 0L

    val updated: Instant
        get() = Instant.fromEpochMilliseconds(updatedAt)

    constructor(
        id: Long,
        name: String,
        eventId: Long,
        position: Int,
        color: String?,
        products: List<ProductEntry>,
        updatedAt: Instant = Now()
    ) : this() {
        this.id = id
        this.name = name
        this.eventId = eventId
        this.position = position
        this.color = color
        this.products = products.toRealmList()
        this.updatedAt = updatedAt.toEpochMilliseconds()
    }
}

internal class ProductEntry() : RealmObject {
    @PrimaryKey
    var id: Long = -1
    var name: String = ""
    var price: Cents = 0
    var soldOut: Boolean = false
    var color: String? = null
    var allergens: RealmList<AllergenEntry> = realmListOf()
    var position: Int = Int.MAX_VALUE

    @Suppress("LongParameterList")
    constructor(
        id: Long,
        name: String,
        price: Cents,
        soldOut: Boolean,
        color: String?,
        allergens: List<AllergenEntry>,
        position: Int,
    ) : this() {
        this.id = id
        this.name = name
        this.price = price
        this.soldOut = soldOut
        this.color = color
        this.allergens = allergens.toRealmList()
        this.position = position
    }
}

internal class AllergenEntry() : RealmObject {
    @PrimaryKey
    var id: Long = -1
    var name: String = ""
    var shortName: String = ""

    constructor(id: Long, name: String, shortName: String) : this() {
        this.id = id
        this.name = name
        this.shortName = shortName
    }
}
