package utils

import kotlin.math.roundToInt

fun Double.roundBy(factor: Double) = (this * factor).roundToInt().toDouble() / factor

fun <T> MutableSet<T>.addAll(vararg args: T) {
    for (arg in args) {
        this.add(arg)
    }
}