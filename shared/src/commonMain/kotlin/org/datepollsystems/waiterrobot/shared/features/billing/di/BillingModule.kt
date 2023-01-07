package org.datepollsystems.waiterrobot.shared.features.billing.di

import org.datepollsystems.waiterrobot.shared.core.di.getApiClient
import org.datepollsystems.waiterrobot.shared.core.di.sharedViewModel
import org.datepollsystems.waiterrobot.shared.features.billing.api.BillingApi
import org.datepollsystems.waiterrobot.shared.features.billing.repository.BillingRepository
import org.datepollsystems.waiterrobot.shared.features.billing.viewmodel.BillingViewModel
import org.koin.core.module.Module
import org.koin.dsl.module

internal val billingModule: Module = module {
    single { BillingApi(getApiClient()) }
    single { BillingRepository(get()) }
    sharedViewModel { params -> BillingViewModel(get(), params.get()) }
}
