package org.datepollsystems.waiterrobot.shared.root.di

import org.datepollsystems.waiterrobot.shared.core.di.sharedViewModelOf
import org.datepollsystems.waiterrobot.shared.root.RootApi
import org.datepollsystems.waiterrobot.shared.root.RootViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val rootModule: Module = module {
    singleOf(::RootApi)
    sharedViewModelOf(::RootViewModel)
}
