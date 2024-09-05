package org.datepollsystems.waiterrobot.shared.utils

import platform.Foundation.NSUUID

actual fun randomUUID(): String = NSUUID().UUIDString
