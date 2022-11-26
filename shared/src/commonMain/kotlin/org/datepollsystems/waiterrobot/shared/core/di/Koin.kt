package org.datepollsystems.waiterrobot.shared.core.di

import co.touchlab.kermit.Logger
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
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

internal fun KoinComponent.injectLogger(tag: String): Lazy<Logger> = inject { parametersOf(tag) }
internal inline fun <reified T> Scope.getWith(vararg params: Any?): T {
    return get(parameters = { parametersOf(*params) })
}
