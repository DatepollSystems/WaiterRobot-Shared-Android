package org.datepollsystems.waiterrobot.shared.core.di

import com.russhwolf.settings.NSUserDefaultsSettings
import com.russhwolf.settings.ObservableSettings
import org.koin.core.module.Module
import org.koin.dsl.module

internal actual val platformModule: Module = module {
    single<ObservableSettings> { NSUserDefaultsSettings.Factory().create() }
}
