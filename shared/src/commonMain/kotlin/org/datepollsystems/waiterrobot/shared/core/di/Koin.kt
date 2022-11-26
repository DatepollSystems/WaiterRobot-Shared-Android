package org.datepollsystems.waiterrobot.shared.core.di

import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.parameter.parametersOf
import org.koin.core.scope.Scope
import org.koin.dsl.module

fun initKoin(appModule: Module = module { }) = startKoin {
    modules(
        appModule,
        platformModule,
        coreModule
    )
}

internal inline fun <reified T> Scope.getWith(vararg params: Any?): T {
    return get(parameters = { parametersOf(*params) })
}
