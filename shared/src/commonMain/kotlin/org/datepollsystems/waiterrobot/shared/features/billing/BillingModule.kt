package org.datepollsystems.waiterrobot.shared.features.billing

import org.datepollsystems.waiterrobot.shared.core.di.sharedViewModelOf
import org.datepollsystems.waiterrobot.shared.features.billing.data.BillingRepositoryImpl
import org.datepollsystems.waiterrobot.shared.features.billing.data.remote.BillingApi
import org.datepollsystems.waiterrobot.shared.features.billing.presentation.BillingViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val billingModule: Module = module {
    singleOf(::BillingApi)
    singleOf(::BillingRepositoryImpl)
    sharedViewModelOf(::BillingViewModel)
}
