package org.datepollsystems.waiterrobot.shared.features.switchevent.di

import org.datepollsystems.waiterrobot.shared.core.di.sharedViewModelOf
import org.datepollsystems.waiterrobot.shared.features.switchevent.api.EventLocationApi
import org.datepollsystems.waiterrobot.shared.features.switchevent.repository.SwitchEventRepository
import org.datepollsystems.waiterrobot.shared.features.switchevent.viewmodel.SwitchEventViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val switchEventModule: Module = module {
    singleOf(::EventLocationApi)
    singleOf(::SwitchEventRepository)
    sharedViewModelOf(::SwitchEventViewModel)
}
