package org.datepollsystems.waiterrobot.shared.features.table.di

import org.datepollsystems.waiterrobot.shared.core.di.sharedViewModel
import org.datepollsystems.waiterrobot.shared.features.table.repository.TableRepository
import org.datepollsystems.waiterrobot.shared.features.table.viewmodel.list.TableListViewModel
import org.koin.core.module.Module
import org.koin.dsl.module

internal val tableModule: Module = module {
    single { TableRepository() }
    sharedViewModel { TableListViewModel(get()) }
}
