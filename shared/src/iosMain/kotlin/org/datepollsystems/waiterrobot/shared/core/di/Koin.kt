package org.datepollsystems.waiterrobot.shared.core.di

import org.koin.core.component.KoinComponent

@Suppress("unused") // Only used by iOS
fun initKoinIos() = initKoin()

@Suppress("unused") // Only used by iOS
object IosKoinComponent : KoinComponent {
}
