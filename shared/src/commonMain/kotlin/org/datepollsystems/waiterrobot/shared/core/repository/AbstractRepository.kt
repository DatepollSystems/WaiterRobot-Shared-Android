package org.datepollsystems.waiterrobot.shared.core.repository

import co.touchlab.kermit.Logger
import org.datepollsystems.waiterrobot.shared.core.di.injectLogger
import org.koin.core.component.KoinComponent

internal abstract class AbstractRepository : KoinComponent {
    protected val logger: Logger by injectLogger(this::class.simpleName!!)
}
