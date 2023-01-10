package org.datepollsystems.waiterrobot.shared.core.di

import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.SharedPreferencesSettings
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

internal actual val platformModule: Module = module {
    single<ObservableSettings> { SharedPreferencesSettings.Factory(androidContext()).create() }
}
