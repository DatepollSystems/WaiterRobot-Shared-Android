package org.datepollsystems.waiterrobot.shared.features.billing.di

import org.datepollsystems.waiterrobot.shared.core.di.sharedViewModelOf
import org.datepollsystems.waiterrobot.shared.features.billing.api.BillingApi
import org.datepollsystems.waiterrobot.shared.features.billing.api.BillingApiV1
import org.datepollsystems.waiterrobot.shared.features.billing.repository.BillingRepository
import org.datepollsystems.waiterrobot.shared.features.billing.viewmodel.BillingViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val billingModule: Module = module {
    singleOf(::BillingApiV1)
    singleOf(::BillingApi)
    singleOf(::BillingRepository)
    sharedViewModelOf(::BillingViewModel)
}
