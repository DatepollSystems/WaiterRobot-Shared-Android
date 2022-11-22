package org.datepollsystems.waiterrobot

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform