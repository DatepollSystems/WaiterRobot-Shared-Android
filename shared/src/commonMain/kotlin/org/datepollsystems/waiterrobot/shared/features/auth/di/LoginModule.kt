package org.datepollsystems.waiterrobot.shared.features.auth.di

import org.datepollsystems.waiterrobot.shared.core.di.sharedViewModelOf
import org.datepollsystems.waiterrobot.shared.features.auth.api.AuthApi
import org.datepollsystems.waiterrobot.shared.features.auth.api.WaiterApi
import org.datepollsystems.waiterrobot.shared.features.auth.repository.AuthRepository
import org.datepollsystems.waiterrobot.shared.features.auth.viewmodel.LoginViewModel
import org.datepollsystems.waiterrobot.shared.features.auth.viewmodel.register.RegisterViewModel
import org.datepollsystems.waiterrobot.shared.features.auth.viewmodel.scanner.LoginScannerViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val loginModule: Module = module {
    singleOf(::AuthApi)
    singleOf(::WaiterApi)
    singleOf(::AuthRepository)
    sharedViewModelOf(::LoginViewModel)
    sharedViewModelOf(::LoginScannerViewModel)
    sharedViewModelOf(::RegisterViewModel)
}
