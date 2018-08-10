package utils

class Random {
    var state: Long

    constructor(seed: Long) {
        state = xorShift64(seed or -0x35014542)
    }

    fun random(): Int {
        val a = state.toInt() and Int.MAX_VALUE
        state = xorShift64(state)
        return a
    }

    fun random(min: Int, max: Int): Int {
        return (random() % (1 + max - min)) + min
    }

    fun random(min: Double, max: Double): Double {
        val r = random().toDouble() / Int.MAX_VALUE
        return r * (max - min) + min
    }

    fun random(range: ClosedRange<Int>): Int {
        return random(range.start, range.endInclusive)
    }

    fun random(range: ClosedRange<Double>): Double {
        return random(range.start, range.endInclusive)
    }

    fun <E> getRandomElement(list: List<E>) = list[random(list.indices)]

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
}