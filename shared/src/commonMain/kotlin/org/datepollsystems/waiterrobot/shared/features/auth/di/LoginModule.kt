package org.datepollsystems.waiterrobot.shared.features.auth.di

import org.datepollsystems.waiterrobot.shared.core.di.CustomKtorLogger
import org.datepollsystems.waiterrobot.shared.features.auth.api.AuthApi
import org.datepollsystems.waiterrobot.shared.features.auth.api.WaiterApi
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal val authClientQualifier = named("authClient")

internal val loginModule: Module = module {
    single(authClientQualifier) {
        createAuthClient(
            json = get(),
            logger = CustomKtorLogger("auth"),
            enableNetworkLogs = true
        )
    }
    single { AuthApi(client = get(authClientQualifier)) }
    single { WaiterApi(client = get(authClientQualifier)) }
}
