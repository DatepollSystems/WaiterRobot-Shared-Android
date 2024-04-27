package org.datepollsystems.waiterrobot.shared.core.di

import com.russhwolf.settings.NSUserDefaultsSettings
import com.russhwolf.settings.ObservableSettings
import dev.icerock.moko.permissions.PermissionsController
import org.koin.core.module.Module
import org.koin.dsl.module
import dev.icerock.moko.permissions.ios.PermissionsController as IosPermissionsController

internal actual val platformModule: Module = module {
    single<ObservableSettings> { NSUserDefaultsSettings.Factory().create() }
    single<PermissionsController> { IosPermissionsController() }
}
