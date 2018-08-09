package client.ui

import model.SGalaxy
import model.SStar
import kotlin.browser.document
import kotlin.browser.window

class Galaxy(val thing: SGalaxy) {
    private val SIZE = SGalaxy.RADIUS * 1.3

    private val root = SVGElement("svg")
    private var width = 0.0
    private var height = 0.0
    private var size = 0.0
    private var scale = 0.0

    init {
        root.element.setAttribute("width", "100%")
        root.element.setAttribute("height", "100%")

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

        if (scale == 0.0) {
            scale = (size / 2) / SIZE
        }

        for (star in thing) {
            if (star is SStar) {
                val r = SVGElement("circle")
//                r.x = star.x * scale + width / 2.0
//                r.y = star.y * scale + height / 2.0
                r.width = 5.0
                r.height = 5.0
                r.stroke = "#fff"
                r["stroke-width"] = "1px"
                r.addTo(root)
            }
        }
    }
}
