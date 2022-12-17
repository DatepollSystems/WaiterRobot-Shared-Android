package org.datepollsystems.waiterrobot.shared.core.extensions

fun String.truncate(maxLength: Int, end: String = "..."): String {
    require(maxLength > end.length) { "maxLength must be grater than the length of end (${end.length})" }
    if (this.length <= maxLength) return this

    return this.take(maxLength - end.length) + end
}
