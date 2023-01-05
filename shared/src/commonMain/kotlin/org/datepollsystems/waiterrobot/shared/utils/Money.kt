package org.datepollsystems.waiterrobot.shared.utils

import kotlin.math.abs

typealias Cents = Int

class Money constructor(
    private val cents: Int
) {
    constructor(euro: Int, cents: Int) : this(
        kotlin.run {
            require(cents in -99..99) { "Cents must be between -99 and 99" }
            val result = abs(euro) * 100 + abs(cents)
            return@run if (euro < 0 || cents < 0) -result else result
        }
    )

    operator fun plus(other: Money): Money = Money(cents + other.cents)

    operator fun minus(other: Money): Money = Money(cents - other.cents)

    operator fun unaryMinus(): Money = Money(-cents)

    operator fun times(n: Int): Money = Money(cents * n)

    /** Use with caution this rounds down to the next full cents */
    operator fun div(n: Int): Money = Money(cents / n)

    fun serialize(): Int = cents

    fun isNegative(): Boolean = cents < 0

    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (other !is Money) return false
        return this.cents == other.cents
    }

    override fun hashCode(): Int = cents.hashCode()

    override fun toString(): String = (if (cents < 0) "- " else "") +
        "${abs(cents) / 100}.${(abs(cents) % 100).toString().padStart(2, '0')} €"
}

val String.euro
    get(): Money {
        val moneyRegex = Regex("^(?:- ?)?(\\d*)(?:[.,](\\d{0,2}))?(?: ?€)?$")
        val match = moneyRegex.find(this)
            ?: throw throw IllegalArgumentException("Cannot convert $this to Money: Invalid format.")

        val euros = match.groupValues[1].ifEmpty { "0" }.toInt()
        val cents = match.groupValues[2].padEnd(2, '0').toInt()
        val result = Money(euros * 100 + cents)

        return if (this.startsWith("-")) -result else result
    }
val Int.euro get() = Money(this * 100)
val Int.cent get() = Money(this)

inline fun <T> Iterable<T>.sumOf(selector: (T) -> Money): Money {
    var sum: Money = 0.euro
    for (element in this) {
        sum += selector(element)
    }
    return sum
}

fun Iterable<Money>.sum(): Money {
    var sum: Money = 0.euro
    for (element in this) {
        sum += element
    }
    return sum
}
