package org.datepollsystems.waiterrobot.shared.core.di

import org.datepollsystems.waiterrobot.shared.core.viewmodel.AbstractViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.definition.Definition
import org.koin.core.module.KoinDefinition
import org.koin.core.module.Module
import org.koin.core.qualifier.Qualifier

actual inline fun <reified T : AbstractViewModel<*, *>> Module.sharedViewModel(
    qualifier: Qualifier?,
    noinline definition: Definition<T>
): KoinDefinition<T> = viewModel(qualifier = qualifier, definition = definition)
