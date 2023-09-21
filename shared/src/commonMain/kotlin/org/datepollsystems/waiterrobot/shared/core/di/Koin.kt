package org.datepollsystems.waiterrobot.shared.core.di

import co.touchlab.kermit.Logger
import org.datepollsystems.waiterrobot.shared.features.auth.di.loginModule
import org.datepollsystems.waiterrobot.shared.features.billing.di.billingModule
import org.datepollsystems.waiterrobot.shared.features.order.di.orderModule
import org.datepollsystems.waiterrobot.shared.features.settings.di.settingsModule
import org.datepollsystems.waiterrobot.shared.features.switchevent.di.switchEventModule
import org.datepollsystems.waiterrobot.shared.features.table.di.tableModule
import org.datepollsystems.waiterrobot.shared.root.di.rootModule
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.core.parameter.parametersOf
import org.koin.dsl.KoinAppDeclaration

fun initKoin(appDeclaration: KoinAppDeclaration = { }) = startKoin {
    appDeclaration()
    modules(
        platformModule,
        coreModule,
        rootModule,
        loginModule,
        switchEventModule,
        tableModule,
        orderModule,
        billingModule,
        settingsModule
    )
}

fun KoinComponent.injectLogger(tag: String): Lazy<Logger> = inject { parametersOf(tag) }
fun KoinComponent.injectLoggerForClass(): Lazy<Logger> =
    injectLogger(this::class.simpleName ?: "AnonymousClass")
