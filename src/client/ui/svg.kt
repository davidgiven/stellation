package client.ui

import org.w3c.dom.Element
import kotlin.browser.document
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

private const val NS = "http://www.w3.org/2000/svg"

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

    constructor(tag: String): this(document.createElementNS(NS, tag))

    var x by doubleProxy
    var y by doubleProxy
    var cx by doubleProxy
    var cy by doubleProxy
    var width by doubleProxy
    var height by doubleProxy
    var stroke by stringProxy

    val clientWidth: Double get() = element.clientWidth.toDouble()
    val clientHeight: Double get() = element.clientHeight.toDouble()

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
