package client.ui

import org.w3c.dom.Element
import org.w3c.dom.asList
import kotlin.browser.document
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

private const val SVG_NS = "http://www.w3.org/2000/svg"
private const val XLINK_NS = "http://www.w3.org/1999/xlink"

private val doubleProxy = object : ReadWriteProperty<SVGElement, Double> {
    override fun setValue(thisRef: SVGElement, property: KProperty<*>, value: Double) =
            thisRef.element.setAttribute(property.name, value.toString())

    override fun getValue(thisRef: SVGElement, property: KProperty<*>) =
            thisRef.element.getAttribute(property.name)!!.toDouble()
}

private val stringProxy = object : ReadWriteProperty<SVGElement, String> {
    override fun setValue(thisRef: SVGElement, property: KProperty<*>, value: String) =
            thisRef.element.setAttribute(property.name, value)

    override fun getValue(thisRef: SVGElement, property: KProperty<*>) =
            thisRef.element.getAttribute(property.name)!!
}

class SVGElement {
    val element: Element

    constructor(element: Element) {
        this.element = element
    }

    constructor(tag: String): this(document.createElementNS(SVG_NS, tag))

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
            val current = classes
            replacement.minus(current).forEach { element.classList.add(it) }
            current.minus(replacement).forEach { element.classList.remove(it) }
        }

    val clientWidth: Double get() = element.clientWidth.toDouble()
    val clientHeight: Double get() = element.clientHeight.toDouble()

    var href: String
        get() = element.getAttributeNS(XLINK_NS, "href")!!
        set(value) { element.setAttributeNS(XLINK_NS, "href", value) }

    operator fun get(name: String) = element.getAttribute(name)
    operator fun set(name: String, value: String) = element.setAttribute(name, value)

    fun addTo(e: Element) {
        e.appendChild(element)
    }

    fun addTo(e: SVGElement) = addTo(e.element)

    fun clear() {
        while (element.lastChild != null) {
            element.removeChild(element.lastChild!!)
        }
    }
}
