package org.datepollsystems.waiterrobot.shared.features.order.di

import org.datepollsystems.waiterrobot.shared.core.di.sharedViewModel
import org.datepollsystems.waiterrobot.shared.features.order.api.OrderApi
import org.datepollsystems.waiterrobot.shared.features.order.api.ProductApi
import org.datepollsystems.waiterrobot.shared.features.order.db.ProductDatabase
import org.datepollsystems.waiterrobot.shared.features.order.repository.OrderRepository
import org.datepollsystems.waiterrobot.shared.features.order.repository.ProductRepository
import org.datepollsystems.waiterrobot.shared.features.order.viewmodel.OrderViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val orderModule: Module = module {
    singleOf(::OrderApi)
    singleOf(::OrderRepository)
    singleOf(::ProductApi)
    singleOf(::ProductRepository)
    singleOf(::ProductDatabase)
    // nullable parameters currently are not supported for the constructor dsl
    sharedViewModel { params -> OrderViewModel(get(), get(), params.get(), params.getOrNull()) }
}
