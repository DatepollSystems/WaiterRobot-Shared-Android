package org.datepollsystems.waiterrobot

class Greeting {
    private val platform: Platform = getPlatform()

    fun greeting(): String {
        return "Hello, ${platform.name}! This is new."
    }
}
