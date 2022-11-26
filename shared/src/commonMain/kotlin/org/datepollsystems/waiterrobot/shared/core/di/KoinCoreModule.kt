package org.datepollsystems.waiterrobot.shared.core.di

import co.touchlab.kermit.Logger
import co.touchlab.kermit.Severity
import co.touchlab.kermit.StaticConfig
import co.touchlab.kermit.platformLogWriter
import org.koin.dsl.module

internal val coreModule = module {
    val baseLogger = Logger(
        // TODO different severity for debug and release build?
        StaticConfig(Severity.Verbose, logWriterList = listOf(platformLogWriter())),
        tag = "WaiterRobot"
    )
    factory { (tag: String?) -> if (tag != null) baseLogger.withTag(tag) else baseLogger }
}
