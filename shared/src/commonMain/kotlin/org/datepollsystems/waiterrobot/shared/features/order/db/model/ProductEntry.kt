package org.datepollsystems.waiterrobot.shared.features.order.db.model

import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import kotlinx.datetime.Instant
import org.datepollsystems.waiterrobot.shared.utils.Cents
import org.datepollsystems.waiterrobot.shared.utils.extensions.Now

internal class ProductEntry() : RealmObject {
    @PrimaryKey
    var id: Long? = null
    var name: String? = null
    var price: Cents? = null
    var soldOut: Boolean? = null
    var updatedAt: Long = 0L
    var eventId: Long? = null
    var allergens: RealmList<Allergen>? = null
    var productGroup: ProductGroup? = null

    val updated: Instant
        get() = Instant.fromEpochMilliseconds(updatedAt)

    constructor(
        id: Long,
        eventId: Long,
        name: String,
        price: Cents,
        soldOut: Boolean,
        productGroup: ProductGroup,
        allergens: RealmList<Allergen>,
        updatedAt: Instant = Now()
    ) : this() {
        this.id = id
        this.eventId = eventId
        this.name = name
        this.price = price
        this.soldOut = soldOut
        this.allergens = allergens
        this.productGroup = productGroup
        this.updatedAt = updatedAt.toEpochMilliseconds()
    }

    internal class Allergen constructor() : RealmObject {
        var id: Long? = null
        var name: String? = null
        var shortName: String? = null

        constructor(id: Long, name: String, shortName: String) : this() {
            this.id = id
            this.name = name
            this.shortName = shortName
        }
    }

    internal class ProductGroup constructor() : RealmObject {
        var id: Long? = null
        var name: String? = null

        constructor(id: Long, name: String) : this() {
            this.id = id
            this.name = name
        }
    }
}
