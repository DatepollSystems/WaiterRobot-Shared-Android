package org.datepollsystems.waiterrobot.shared.features.table.repository

import org.datepollsystems.waiterrobot.shared.core.repository.AbstractRepository
import org.datepollsystems.waiterrobot.shared.features.table.api.TableApi
import org.datepollsystems.waiterrobot.shared.features.table.models.Table

internal class TableRepository(private val tableApi: TableApi) : AbstractRepository() {

    suspend fun getTables(forceUpdate: Boolean): List<Table> {
        // TODO get from CacheDB
        return tableApi.getTables().map { Table(it.id, it.number) }
    }
}
