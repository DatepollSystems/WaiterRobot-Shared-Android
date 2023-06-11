package org.datepollsystems.waiterrobot.shared.utils.extensions

fun <T : Any> T?.defaultOnNull(default: T): T = this ?: default
