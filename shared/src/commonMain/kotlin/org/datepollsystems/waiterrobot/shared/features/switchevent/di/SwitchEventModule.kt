package org.datepollsystems.waiterrobot.shared.features.switchevent.di

import org.datepollsystems.waiterrobot.shared.core.di.getApiClient
import org.datepollsystems.waiterrobot.shared.core.di.sharedViewModel
import org.datepollsystems.waiterrobot.shared.features.switchevent.api.EventLocationApi
import org.datepollsystems.waiterrobot.shared.features.switchevent.repository.SwitchEventRepository
import org.datepollsystems.waiterrobot.shared.features.switchevent.viewmodel.SwitchEventViewModel
import org.koin.core.module.Module
import org.koin.dsl.module

internal val switchEventModule: Module = module {
    single { EventLocationApi(getApiClient()) }
    single { SwitchEventRepository(get()) }
    sharedViewModel { SwitchEventViewModel(get()) }
}
