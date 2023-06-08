package org.datepollsystems.waiterrobot.shared.root.di

import org.datepollsystems.waiterrobot.shared.core.di.getApiClient
import org.datepollsystems.waiterrobot.shared.core.di.sharedViewModel
import org.datepollsystems.waiterrobot.shared.root.RootApi
import org.datepollsystems.waiterrobot.shared.root.RootViewModel
import org.koin.core.module.Module
import org.koin.dsl.module

internal val rootModule: Module = module {
    single { RootApi(getApiClient()) }
    sharedViewModel { RootViewModel(get(), get()) }
}
