package org.datepollsystems.waiterrobot.shared.features.auth.di

import org.datepollsystems.waiterrobot.shared.core.di.CustomKtorLogger
import org.datepollsystems.waiterrobot.shared.core.di.getApiClient
import org.datepollsystems.waiterrobot.shared.core.di.sharedViewModel
import org.datepollsystems.waiterrobot.shared.features.auth.api.AuthApi
import org.datepollsystems.waiterrobot.shared.features.auth.api.WaiterApi
import org.datepollsystems.waiterrobot.shared.features.auth.repository.AuthRepository
import org.datepollsystems.waiterrobot.shared.features.auth.viewmodel.LoginViewModel
import org.datepollsystems.waiterrobot.shared.features.auth.viewmodel.scanner.LoginScannerViewModel
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

private val authClientQualifier = named("authClient")

internal val loginModule: Module = module {
    single(authClientQualifier) {
        createAuthClient(
            json = get(),
            logger = CustomKtorLogger("auth"),
            enableNetworkLogs = true
        )
    }
    single { AuthApi(client = get(authClientQualifier)) }
    single { WaiterApi(client = getApiClient()) }
    single { AuthRepository(get()) }
    sharedViewModel { LoginViewModel() }
    sharedViewModel { LoginScannerViewModel(get()) }
}
