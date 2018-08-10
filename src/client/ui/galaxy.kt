package client.ui

import model.SGalaxy
import model.SStar
import kotlin.browser.document
import kotlin.browser.window

class Galaxy(val thing: SGalaxy) {
    private val SIZE = SGalaxy.RADIUS * 1.1

    private val root = SVGElement("svg")
    private var width = 0.0
    private var height = 0.0
    private var size = 0.0
    private var scale = 0.0

    private val defs = renderDefs()

    init {
        root.classes = setOf("map")
        window.addEventListener("resize", { render() })
    }

    fun attach() {
        document.body!!.insertBefore(root.element, document.body!!.firstElementChild)
        render()
    }

    fun render() {
        root.clear()
        width = root.clientWidth
        height = root.clientHeight
        size = minOf(width, height)
        root["viewBox"] = "0 0 $width $height"
        defs.addTo(root)

        if (scale == 0.0) {
            scale = (size / 2) / SIZE
        }

        val mapGroup = SVGElement("g")
        mapGroup.addTo(root)

        var ox = width / 2.0
        var oy = height / 2.0

        run {
            val step = 10.0
            var radius = step
            while (radius < SGalaxy.RADIUS * 2.0) {
                var r = SVGElement("circle")
                r.classes = setOf("graticule")
                r.cx = ox
                r.cy = oy
                r.r = radius * scale
                r.addTo(mapGroup)

                radius += step
            }
        }

        run {
            val size = SGalaxy.RADIUS * 2.0
            val step = 5.0
            var offset = -size
            while (offset < size) {
                val rh = SVGElement("line")
                rh.classes = setOf("graticule")
                rh.x1 = -size * scale + ox
                rh.y1 = offset * scale + oy
                rh.x2 = size * scale + ox
                rh.y2 = offset * scale + oy
                rh.addTo(mapGroup)

                val rv = SVGElement("line")
                rv.classes = setOf("graticule")
                rv.x1 = offset * scale + ox
                rv.y1 = -size * scale + oy
                rv.x2 = offset * scale + ox
                rv.y2 = size * scale + oy
                rv.addTo(mapGroup)

                offset += step
            }
        }

        for (star in thing) {
            if (star is SStar) {
                val r = SVGElement("use")
                val b = star.brightness.toInt()
                r.href = "#star-$b"
                r.x = star.x * scale + width / 2.0
                r.y = star.y * scale + height / 2.0
                r.addTo(mapGroup)
            }
        }
    }

    private fun renderDefs(): SVGElement {
        val defs = SVGElement("defs")

        fun makeStar(id: String): SVGElement {
            val star = SVGElement("g")
            star.addTo(defs)
            star.id = id
            star.classes = setOf("star", id)
            return star
        }

        run {
            val star = makeStar("star-0")
            val c = SVGElement("circle")
            c.addTo(star)
            c.cx = 0.0
            c.cy = 0.0
            c.r = 5.0
        }

        run {
            val star = makeStar("star-1")
            val c = SVGElement("circle")
            c.addTo(star)
            c.cx = 0.0
            c.cy = 0.0
            c.r = 2.0
        }

        run {
            val star = makeStar("star-2")
            val c = SVGElement("circle")
            c.addTo(star)
            c.cx = 0.0
            c.cy = 0.0
            c.r = 2.0
        }

        for (spokes in 3..9) {
            val star = makeStar("star-$spokes")
            for (i in 0 until spokes) {
                val spoke = SVGElement("line")
                spoke.addTo(star)
                spoke.x1 = 0.0
                spoke.y1 = 0.0
                spoke.x2 = 0.0
                spoke.y2 = 5.0
                val angle = i.toDouble() * 360.0 / spokes.toDouble()
                spoke["transform"] = "rotate($angle)"
            }
        }

        return defs
    }
}
