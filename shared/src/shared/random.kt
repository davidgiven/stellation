package shared

private var state = xorShift64(runtime.nanotime() or -0x35014542)

fun random(min: Int, max: Int): Int {
    val a = state.toInt() and Int.MAX_VALUE
    state = xorShift64(state)
    return (a % (1 + max - min)) + min
}

fun random(range: ClosedRange<Int>): Int {
    return random(range.start, range.endInclusive)
}

/**
 * XORShift algorithm - credit to George Marsaglia!
 * @param a Initial state
 * @return new state
 */
private fun xorShift64(a: Long): Long {
    var b = a
    b = b xor (b shl 21)
    b = b xor b.ushr(35)
    b = b xor (b shl 4)
    return b
}