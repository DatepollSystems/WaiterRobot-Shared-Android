package org.datepollsystems.waiterrobot.shared.core.di

import co.touchlab.kermit.Logger
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import org.datepollsystems.waiterrobot.shared.features.auth.di.loginModule
import org.datepollsystems.waiterrobot.shared.features.order.di.orderModule
import org.datepollsystems.waiterrobot.shared.features.switchevent.di.switchEventModule
import org.datepollsystems.waiterrobot.shared.features.table.di.tableModule
import org.datepollsystems.waiterrobot.shared.root.di.rootModule
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.core.definition.Definition
import org.koin.core.module.KoinDefinition
import org.koin.core.module.Module
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.Qualifier
import org.koin.core.scope.Scope
import org.koin.dsl.module

fun initKoin(appModule: Module = module { }) = startKoin {
    modules(
        appModule,
        platformModule,
        coreModule,
        rootModule,
        loginModule,
        switchEventModule,
        tableModule,
        orderModule,
    )
}

internal fun KoinComponent.injectLogger(tag: String): Lazy<Logger> = inject { parametersOf(tag) }
internal fun KoinComponent.injectLoggerForClass(): Lazy<Logger> =
    injectLogger(this::class.simpleName!!)

internal inline fun <reified T> Scope.getWith(vararg params: Any?): T {
    return get(parameters = { parametersOf(*params) })
}

internal expect inline fun <reified T : ViewModel> Module.sharedViewModel(
    qualifier: Qualifier? = null,
    noinline definition: Definition<T>
): KoinDefinition<T>
