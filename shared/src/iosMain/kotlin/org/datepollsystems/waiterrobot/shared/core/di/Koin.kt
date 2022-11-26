package org.datepollsystems.waiterrobot.shared.core.di

import co.touchlab.kermit.Logger
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.parameter.parametersOf

@Suppress("unused") // Only used by iOS
fun initKoinIos() = initKoin()

@Suppress("unused") // Only used by iOS
object IosKoinComponent : KoinComponent {
    fun logger(tag: String): Logger = get { parametersOf(tag) }
}
