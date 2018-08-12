package client.ui

import interfaces.log
import model.SGalaxy
import model.SStar
import org.w3c.dom.DOMMatrix
import org.w3c.dom.DOMPoint
import org.w3c.dom.events.MouseEvent
import org.w3c.dom.events.WheelEvent
import org.w3c.dom.svg.SVGGElement
import org.w3c.dom.svg.SVGGraphicsElement
import org.w3c.dom.svg.SVGSVGElement
import org.w3c.dom.svg.SVGTextElement
import kotlin.browser.document
import kotlin.browser.window

private const val SCALE_STEP = 1.2

private data class StarSlot(
        var star: SVGE<SVGGraphicsElement>,
        var text: SVGE<SVGTextElement>)

class Galaxy(val thing: SGalaxy) {
    private val SIZE = SGalaxy.RADIUS * 1.1

    private val cache = HashMap<SStar, StarSlot>()

    private val root = SVGE<SVGSVGElement>("svg")
    private val mapGroup = SVGE<SVGGElement>("g").addTo(root)
    private var mapToScreenTransform: DOMMatrix = root.element.createSVGMatrix()
    private var screenToMapTransform: DOMMatrix = root.element.createSVGMatrix()
    private var graticule = createGraticule().addTo(mapGroup)
    private var width = 0.0
    private var height = 0.0
    private var centreX = 0.0
    private var centreY = 0.0
    private var size = 0.0
    private var scale = 0.0

    init {
        root.classes = setOf("map")
        window.onresize = { update() }
        root.element.addEventListener("wheel", { onWheelEvent(it as WheelEvent) })
        root.element.addEventListener("mousedown", { onDrag(it as MouseEvent) })
        root.element.addEventListener("mousemove", { onMove(it as MouseEvent) })
    }

    fun attach() {
        document.body!!.insertBefore(root.element, document.body!!.firstElementChild)
        update()
    }

    private fun createGraticule(): SVGE<SVGGraphicsElement> {
        val graticuleElement = SVGE<SVGGraphicsElement>("use")
        graticuleElement.href = "#svg-graticule"
        return graticuleElement
    }

    private fun findOrCreateStarSlot(star: SStar) =
            cache.getOrPut(star) {
                val starElement = SVGE<SVGGraphicsElement>("use")
                val b = star.brightness.toInt()
                starElement.href = "#svg-star-$b"
                starElement.addTo(mapGroup)

                val textElement = SVGE<SVGTextElement>("text")
                textElement.text = star.name.toUpperCase()
                textElement.classes = setOf("star")
                textElement.addTo(mapGroup)

                StarSlot(starElement, textElement)
            }

    fun setPanPosition() {
        var ox = -centreX * scale
        var oy = -centreY * scale

        mapGroup["transform"] = "translate($ox,$oy)"
        mapToScreenTransform = graticule.element.getScreenCTM()!!
        screenToMapTransform = mapToScreenTransform.inverse()
    }

    fun update() {
        val (w, h) = root.clientSize
        width = w
        height = h
        size = minOf(width, height)
        root["viewBox"] = "${-width / 2} ${-height / 2} $width $height"

        if (scale == 0.0) {
            scale = (size / 2) / SIZE
        }

        graticule["transform"] = "scale($scale)"
        setPanPosition()

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
            /* We want to scale around the mouse position. */
            val mouseSPt = point(event.clientX, event.clientY)
            val centreSPt = point(centreX, centreY).matrixTransform(mapToScreenTransform)
            /* mouseSpt - centreSpt needs to remain constant after the scale. */
            val displacementX = mouseSPt.x - centreSPt.x
            val displacementY = mouseSPt.y - centreSPt.y

            /* Put the mouse position at the centre of the map. */
            centreX += displacementX/scale
            centreY += displacementY/scale

            scale *= scaleDelta

            /* Adjust back again so the invariant position is under the mouse pointer. */
            centreX -= displacementX/scale
            centreY -= displacementY/scale

            update()
        }
    }

    private inline fun point(x: Double, y: Double) = DOMPoint(x, y)
    private inline fun point(x: Int, y: Int) = point(x.toDouble(), y.toDouble())

    private fun onMove(event: MouseEvent) {
        val screenPt = point(event.clientX, event.clientY)
        val svgPt = screenPt.matrixTransform(screenToMapTransform)
        log("mouseX=${svgPt.x} mouseY=${svgPt.y}")
    }

    private fun onDrag(event: MouseEvent) {
        event.preventDefault()

        val dragStartX = event.clientX
        val dragStartY = event.clientY
        val centreStartX = centreX
        val centreStartY = centreY

        document.onmousemove = {
            val me = it as MouseEvent
            me.preventDefault()
            val offsetX = me.clientX - dragStartX
            val offsetY = me.clientY - dragStartY
            centreX = centreStartX - offsetX / scale
            centreY = centreStartY - offsetY / scale
            setPanPosition()
            false
        }

        document.onmouseup = {
            val me = it as MouseEvent
            me.preventDefault()
            document.onmousemove = null
            document.onmouseup = null
            false
        }
    }
}
