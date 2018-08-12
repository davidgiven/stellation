@file:Suppress("NOTHING_TO_INLINE")

package client.ui

import org.w3c.dom.Element
import org.w3c.dom.Text
import org.w3c.dom.asList
import org.w3c.dom.svg.SVGElement
import org.w3c.dom.svg.SVGSVGElement
import kotlin.browser.document
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

const val SVG_NS = "http://www.w3.org/2000/svg"
const val XLINK_NS = "http://www.w3.org/1999/xlink"

private val doubleProxy = object : ReadWriteProperty<SVGE<*>, Double> {
    override fun setValue(thisRef: SVGE<*>, property: KProperty<*>, value: Double) =
            thisRef.element.setAttribute(property.name, value.toString())

    override fun getValue(thisRef: SVGE<*>, property: KProperty<*>) =
            thisRef.element.getAttribute(property.name)!!.toDouble()
}

private val stringProxy = object : ReadWriteProperty<SVGE<*>, String> {
    override fun setValue(thisRef: SVGE<*>, property: KProperty<*>, value: String) =
            thisRef.element.setAttribute(property.name, value)

    override fun getValue(thisRef: SVGE<*>, property: KProperty<*>) =
            thisRef.element.getAttribute(property.name)!!
}

class SVGE<T: SVGElement> {
    val element: T

    constructor(element: T) {
        this.element = element
    }

    constructor(tag: String): this(document.createElementNS(SVG_NS, tag) as T)

    var id by stringProxy

    var x by doubleProxy
    var y by doubleProxy
    var cx by doubleProxy
    var cy by doubleProxy
    var r by doubleProxy
    var width by doubleProxy
    var height by doubleProxy
    var x1 by doubleProxy
    var y1 by doubleProxy
    var x2 by doubleProxy
    var y2 by doubleProxy

    var classes: Set<String>
        get() = element.classList.asList().toSet()
        set(replacement) {
            element.classList.value = replacement.joinToString(" ")
        }

    val clientSize: Pair<Double, Double> get() {
        val rect = element.getBoundingClientRect()
        return Pair(rect.width, rect.height)
    }

    var href: String
        inline get() = element.getAttributeNS(XLINK_NS, "href")!!
        inline set(value) { element.setAttributeNS(XLINK_NS, "href", value) }

    var text: String
        get() = (element.firstChild as Text).textContent!!
        set(value) {
            if (element.childElementCount == 0) {
                element.appendChild(document.createTextNode(value))
            } else {
                (element.firstChild as Text).textContent = value
            }
        }

    inline operator fun get(name: String) = element.getAttribute(name)
    inline operator fun set(name: String, value: String) = element.setAttribute(name, value)
}

inline fun <T: SVGElement> SVGE<T>.addTo(e: Element): SVGE<T> {
    e.appendChild(element)
    return this
}

inline fun <T: SVGElement> SVGE<T>.addTo(e: SVGE<*>) = addTo(e.element)

