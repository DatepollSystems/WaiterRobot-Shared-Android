package org.datepollsystems.waiterrobot.shared.features.switchevent

import org.datepollsystems.waiterrobot.shared.core.di.sharedViewModelOf
import org.datepollsystems.waiterrobot.shared.features.switchevent.data.SwitchEventRepositoryImpl
import org.datepollsystems.waiterrobot.shared.features.switchevent.data.remote.EventLocationApi
import org.datepollsystems.waiterrobot.shared.features.switchevent.domain.repository.SwitchEventRepository
import org.datepollsystems.waiterrobot.shared.features.switchevent.presentation.SwitchEventViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal val switchEventModule: Module = module {
    singleOf(::EventLocationApi)
    singleOf(::SwitchEventRepositoryImpl) bind SwitchEventRepository::class
    sharedViewModelOf(::SwitchEventViewModel)
}
