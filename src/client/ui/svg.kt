@file:Suppress("NOTHING_TO_INLINE")

package client.ui

import org.w3c.dom.Element
import org.w3c.dom.Text
import org.w3c.dom.asList
import org.w3c.dom.svg.SVGElement
import kotlin.browser.document
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

const val SVG_NS = "http://www.w3.org/2000/svg"
const val XLINK_NS = "http://www.w3.org/1999/xlink"

private val doubleProxy = object : ReadWriteProperty<SVGElement, Double> {
    override fun setValue(thisRef: SVGElement, property: KProperty<*>, value: Double) =
            thisRef.setAttribute(property.name, value.toString())

    override fun getValue(thisRef: SVGElement, property: KProperty<*>) =
            thisRef.getAttribute(property.name)!!.toDouble()
}

private val stringProxy = object : ReadWriteProperty<SVGElement, String> {
    override fun setValue(thisRef: SVGElement, property: KProperty<*>, value: String) =
            thisRef.setAttribute(property.name, value)

    override fun getValue(thisRef: SVGElement, property: KProperty<*>) =
            thisRef.getAttribute(property.name)!!
}

inline fun <reified T : SVGElement> createSVGElement(tag: String): T = document.createElementNS(SVG_NS, tag) as T

var SVGElement.id by stringProxy

var SVGElement.x by doubleProxy
var SVGElement.y by doubleProxy
var SVGElement.cx by doubleProxy
var SVGElement.cy by doubleProxy
var SVGElement.r by doubleProxy
var SVGElement.width by doubleProxy
var SVGElement.height by doubleProxy
var SVGElement.x1 by doubleProxy
var SVGElement.y1 by doubleProxy
var SVGElement.x2 by doubleProxy
var SVGElement.y2 by doubleProxy

var SVGElement.classes: Set<String>
    inline get() = classList.asList().toSet()
    inline set(replacement) {
        classList.value = replacement.joinToString(" ")
    }

val SVGElement.clientSize: Pair<Double, Double>
    inline get() {
        val rect = getBoundingClientRect()
        return Pair(rect.width, rect.height)
    }

var SVGElement.href: String
    inline get() = getAttributeNS(XLINK_NS, "href")!!
    inline set(value) {
        setAttributeNS(XLINK_NS, "href", value)
    }

var SVGElement.text: String
    inline get() = (firstChild as Text).textContent!!
    inline set(value) {
        if (childElementCount == 0) {
            appendChild(document.createTextNode(value))
        } else {
            (firstChild as Text).textContent = value
        }
    }

inline operator fun SVGElement.get(name: String) = getAttribute(name)
inline operator fun SVGElement.set(name: String, value: String) = setAttribute(name, value)

inline fun <T : SVGElement> T.addTo(e: Element): T {
    e.appendChild(this)
    return this
}


