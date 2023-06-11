package org.datepollsystems.waiterrobot.shared.features.table.di

import org.datepollsystems.waiterrobot.shared.core.di.sharedViewModelOf
import org.datepollsystems.waiterrobot.shared.features.billing.di.billingModule
import org.datepollsystems.waiterrobot.shared.features.table.api.TableApi
import org.datepollsystems.waiterrobot.shared.features.table.db.TableDatabase
import org.datepollsystems.waiterrobot.shared.features.table.repository.TableRepository
import org.datepollsystems.waiterrobot.shared.features.table.viewmodel.detail.TableDetailViewModel
import org.datepollsystems.waiterrobot.shared.features.table.viewmodel.list.TableListViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val tableModule: Module = module {
    includes(billingModule)

    singleOf(::TableRepository)
    singleOf(::TableDatabase)
    singleOf(::TableApi)
    sharedViewModelOf(::TableListViewModel)
    sharedViewModelOf(::TableDetailViewModel)
}
