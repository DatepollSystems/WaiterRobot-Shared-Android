package org.datepollsystems.waiterrobot.shared.core.di

import dev.icerock.moko.mvvm.viewmodel.ViewModel
import org.koin.core.definition.Definition
import org.koin.core.definition.KoinDefinition
import org.koin.core.module.Module
import org.koin.core.module.dsl.DefinitionOptions
import org.koin.core.module.dsl.new
import org.koin.core.module.dsl.onOptions
import org.koin.core.qualifier.Qualifier

internal expect inline fun <reified T : ViewModel> Module.sharedViewModel(
    qualifier: Qualifier? = null,
    noinline definition: Definition<T>
): KoinDefinition<T>

internal inline fun <reified R : ViewModel> Module.sharedViewModelOf(
    crossinline constructor: () -> R,
    noinline options: DefinitionOptions<R>? = null,
): KoinDefinition<R> = sharedViewModel { new(constructor) }.onOptions(options)

/**
 * @see sharedViewModelOf
 */
internal inline fun <reified R : ViewModel, reified T1> Module.sharedViewModelOf(
    crossinline constructor: (T1) -> R,
    noinline options: DefinitionOptions<R>? = null,
): KoinDefinition<R> = sharedViewModel { new(constructor) }.onOptions(options)

/**
 * @see sharedViewModelOf
 */
internal inline fun <reified R : ViewModel, reified T1, reified T2> Module.sharedViewModelOf(
    crossinline constructor: (T1, T2) -> R,
    noinline options: DefinitionOptions<R>? = null,
): KoinDefinition<R> = sharedViewModel { new(constructor) }.onOptions(options)

/**
 * @see sharedViewModelOf
 */
internal inline fun <reified R : ViewModel, reified T1, reified T2, reified T3> Module.sharedViewModelOf(
    crossinline constructor: (T1, T2, T3) -> R,
    noinline options: DefinitionOptions<R>? = null,
): KoinDefinition<R> = sharedViewModel { new(constructor) }.onOptions(options)

/**
 * @see sharedViewModelOf
 */
internal inline fun <reified R : ViewModel, reified T1, reified T2, reified T3, reified T4> Module.sharedViewModelOf(
    crossinline constructor: (T1, T2, T3, T4) -> R,
    noinline options: DefinitionOptions<R>? = null,
): KoinDefinition<R> = sharedViewModel { new(constructor) }.onOptions(options)
