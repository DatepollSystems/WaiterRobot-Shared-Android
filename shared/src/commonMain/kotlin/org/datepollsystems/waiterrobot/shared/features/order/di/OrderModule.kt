package org.datepollsystems.waiterrobot.shared.features.order.di

import org.datepollsystems.waiterrobot.shared.core.di.getApiClient
import org.datepollsystems.waiterrobot.shared.core.di.sharedViewModel
import org.datepollsystems.waiterrobot.shared.features.order.api.OrderApi
import org.datepollsystems.waiterrobot.shared.features.order.api.ProductApi
import org.datepollsystems.waiterrobot.shared.features.order.repository.OrderRepository
import org.datepollsystems.waiterrobot.shared.features.order.repository.ProductRepository
import org.datepollsystems.waiterrobot.shared.features.order.viewmodel.OrderViewModel
import org.koin.core.module.Module
import org.koin.dsl.module

internal val orderModule: Module = module {
    single { OrderApi(getApiClient()) }
    single { OrderRepository(get()) }
    single { ProductApi(getApiClient()) }
    single { ProductRepository(get()) }
    sharedViewModel { params -> OrderViewModel(get(), get(), params.get(), params.getOrNull()) }
}
