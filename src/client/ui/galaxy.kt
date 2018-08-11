package client.ui

import model.SGalaxy
import model.SStar
import org.w3c.dom.events.WheelEvent
import kotlin.browser.document
import kotlin.browser.window
import kotlin.math.pow

private const val SCALE_STEP = 1.005

class Galaxy(val thing: SGalaxy) {
    private val SIZE = SGalaxy.RADIUS * 1.1

    private val root = SVGElement("svg")
    private var width = 0.0
    private var height = 0.0
    private var panX = 0.0
    private var panY = 0.0
    private var size = 0.0
    private var scale = 0.0

    init {
        root.classes = setOf("map")
        window.addEventListener("resize", { render() })
        window.addEventListener("wheel", { onWheelEvent(it as WheelEvent) })
    }

    fun attach() {
        document.body!!.insertBefore(root.element, document.body!!.firstElementChild)
        render()
    }

    fun render() {
        root.clear()
        val (w, h) = root.clientSize
        width = w
        height = h
        size = minOf(width, height)
        root["viewBox"] = "0 0 $width $height"

        if (scale == 0.0) {
            scale = (size / 2) / SIZE
        }

        var ox = width / 2.0
        var oy = height / 2.0

        val mapGroup = SVGElement("g")
        mapGroup.addTo(root)
        mapGroup["transform"] = "translate($ox,$oy)"

        val r = SVGElement("use")
        r.addTo(mapGroup)
        r.href = "#svg-graticule"
        r["transform"] = "scale($scale)"

        for (star in thing) {
            if (star is SStar) {
                val r = SVGElement("use")
                val b = star.brightness.toInt()
                r.href = "#svg-star-$b"
                val sx = star.x * scale
                val sy = star.y * scale
                r["transform"] = "translate($sx,$sy) scale(5)"
                r.addTo(mapGroup)

                val t = SVGElement("text")
                t.text = star.name.toUpperCase()
                t.classes = setOf("star")
                val ty = sy + 10
                t["transform"] = "translate($sx,$ty)"
                t.addTo(mapGroup)
            }
        }
    }

    fun onWheelEvent(event: WheelEvent) {
        val scaleDelta = SCALE_STEP.pow(-event.deltaY)
        if (scaleDelta != 0.0) {
            scale *= scaleDelta
            render()
        }
    }
}
