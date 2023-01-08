package org.datepollsystems.waiterrobot.shared.features.settings.di

import org.datepollsystems.waiterrobot.shared.core.di.sharedViewModel
import org.datepollsystems.waiterrobot.shared.features.order.di.orderModule
import org.datepollsystems.waiterrobot.shared.features.settings.viewmodel.SettingsViewModel
import org.datepollsystems.waiterrobot.shared.features.table.di.tableModule
import org.koin.core.module.Module
import org.koin.dsl.module

internal val settingsModule: Module = module {
    includes(tableModule)
    includes(orderModule)

    sharedViewModel { SettingsViewModel(get(), get()) }
}
