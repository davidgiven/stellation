package shared

import kotlin.math.roundToInt

fun Double.roundBy(factor: Double) = (this * factor).roundToInt().toDouble() / factor
