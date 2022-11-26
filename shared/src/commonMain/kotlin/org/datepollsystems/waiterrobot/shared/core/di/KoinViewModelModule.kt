package org.datepollsystems.waiterrobot.shared.core.di

import org.datepollsystems.waiterrobot.shared.core.viewmodel.AbstractViewModel
import org.koin.core.definition.Definition
import org.koin.core.module.KoinDefinition
import org.koin.core.module.Module
import org.koin.core.qualifier.Qualifier
import org.koin.dsl.module

internal val viewModelModule: Module = module {

}

expect inline fun <reified T : AbstractViewModel<*, *>> Module.sharedViewModel(
    qualifier: Qualifier? = null,
    noinline definition: Definition<T>
): KoinDefinition<T>
