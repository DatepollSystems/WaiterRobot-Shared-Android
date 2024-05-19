package org.datepollsystems.waiterrobot.shared.features.stripe.di

import org.datepollsystems.waiterrobot.shared.core.CommonApp
import org.datepollsystems.waiterrobot.shared.core.di.sharedViewModel
import org.datepollsystems.waiterrobot.shared.features.stripe.api.StripeApi
import org.datepollsystems.waiterrobot.shared.features.stripe.api.StripeService
import org.datepollsystems.waiterrobot.shared.features.stripe.viewmodel.StripeInitializationViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val stripeModule: Module = module {
    singleOf(::StripeApi)
    singleOf(::StripeService)
    sharedViewModel {
        StripeInitializationViewModel(
            stripe = CommonApp.stripeProvider!!,
            permissionsController = get()
        )
    }
}
