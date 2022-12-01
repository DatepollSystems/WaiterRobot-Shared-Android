package org.datepollsystems.waiterrobot.shared.features.table.repository

import kotlinx.coroutines.delay
import org.datepollsystems.waiterrobot.shared.core.repository.AbstractRepository
import org.datepollsystems.waiterrobot.shared.features.table.models.Table

internal class TableRepository : AbstractRepository() {

    suspend fun getTables(forceUpdate: Boolean): List<Table> {
        // TODO get from API/CacheDB
        delay(1000) // Simulate some network/db call delay

        return (0..10L).map { Table(it, it + 1) }
    }
}
