package model

import interfaces.Oid

enum class Location {
    EMPTY,
    MIDDLE,
    EDGE,
}

class Frame {
    val width: Int
    val height: Int
    val data: Array<Location>

    constructor(width: Int, height: Int) {
        this.width = width
        this.height = height
        this.data = Array(width * height) { Location.EMPTY }
    }

    constructor(rep: String) {
        val s = rep.split('#')
        check(s.size == 3)
        this.width = s[0].toInt()
        this.height = s[1].toInt()
        val encoded = s[2]
        this.data = Array(width * height) { Location.EMPTY }
        check(encoded.length == (width * height))

        for (y in 0..(height - 1)) {
            for (x in 0..(width - 1)) {
                val l = encoded[y * width + x]
                data[y * width + x] = fromRep(l)
            }
        }
    }

    fun serialise(): String {
        val sb = StringBuilder()
        sb.append(width)
        sb.append('#')
        sb.append(height)
        sb.append('#')
        data.forEach { sb.append(toRep(it)) }
        return sb.toString()
    }

    private fun fromRep(c: Char): Location =
            when (c) {
                '0'  -> Location.EMPTY
                '1'  -> Location.MIDDLE
                '2'  -> Location.EDGE
                else -> throw IllegalArgumentException("bad frame representation '$c'")
            }

    private fun toRep(l: Location): Char =
            when (l) {
                Location.EMPTY  -> '0'
                Location.MIDDLE -> '1'
                Location.EDGE   -> '2'
            }
}

open class SFrame(oid: Oid) : SThing(oid) {
    var name by primitive(NAME)
    var data by primitive(DATA)

    val frame: Frame
        get() = Frame(data)
}
