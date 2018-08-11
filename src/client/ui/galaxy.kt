package client.ui

import model.SGalaxy
import model.SStar
import org.w3c.dom.events.WheelEvent
import kotlin.browser.document
import kotlin.browser.window

private const val SCALE_STEP = 1.2

private data class StarSlot(
        var star: SVGElement,
        var text: SVGElement)

class Galaxy(val thing: SGalaxy) {
    private val SIZE = SGalaxy.RADIUS * 1.1

    private val cache = HashMap<SStar, StarSlot>()

    private val root = SVGElement("svg")
    private val mapGroup = SVGElement("g").addTo(root)
    private var graticule = createGraticule().addTo(mapGroup)
    private var width = 0.0
    private var height = 0.0
    private var panX = 0.0
    private var panY = 0.0
    private var size = 0.0
    private var scale = 0.0

    init {
        root.classes = setOf("map")
        window.onresize = { update() }
        root.element.addEventListener("wheel", { onWheelEvent(it as WheelEvent) })
    }

    fun attach() {
        document.body!!.insertBefore(root.element, document.body!!.firstElementChild)
        update()
    }

    private fun createGraticule(): SVGElement {
        val graticuleElement = SVGElement("use")
        graticuleElement.href = "#svg-graticule"
        return graticuleElement
    }

    private fun findOrCreateStarSlot(star: SStar) =
            cache.getOrPut(star) {
                val starElement = SVGElement("use")
                val b = star.brightness.toInt()
                starElement.href = "#svg-star-$b"
                starElement.addTo(mapGroup)

                val textElement = SVGElement("text")
                textElement.text = star.name.toUpperCase()
                textElement.classes = setOf("star")
                textElement.addTo(mapGroup)

                StarSlot(starElement, textElement)
            }

    fun update() {
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

        mapGroup["transform"] = "translate($ox,$oy)"
        graticule["transform"] = "scale($scale)"

        for (star in thing) {
            if (star is SStar) {
                val slot = findOrCreateStarSlot(star)
                val sx = star.x * scale
                val sy = star.y * scale
                slot.star["transform"] = "translate($sx,$sy) scale(5)"

                val ty = sy + 10
                slot.text["transform"] = "translate($sx,$ty)"
            }
        }
    }

    private fun onWheelEvent(event: WheelEvent) {
        val scaleDelta = if (event.deltaY < 0) SCALE_STEP else 1.0 / SCALE_STEP
        if (scaleDelta != 0.0) {
            scale *= scaleDelta
            update()
        }
    }
}
