package org.datepollsystems.waiterrobot.shared.features.table.models

// TODO do we even need those models, can't we just use an interface which defines the exposed
//  properties and then the db model implements that?
//  - Or might there be cases where we want a model to have capabilities that the db entry should not have.
//  - Or would this introduce a difference between the handling of certain features, as they do not rely on db?
data class TableGroup(
    val id: Long,
    val name: String,
    val eventId: Long,
    val position: Int,
    val color: String?,
    val hidden: Boolean,
    var tables: List<Table>
)
