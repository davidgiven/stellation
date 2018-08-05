package runtime.js

import interfaces.IUi
import interfaces.IUiElement
import interfaces.IUiNode
import interfaces.IUiText
import interfaces.UiDragCallbacks
import interfaces.UiElementConstructor
import interfaces.UiTextConstructor
import org.w3c.dom.HTMLElement
import org.w3c.dom.asList
import org.w3c.dom.events.KeyboardEvent
import org.w3c.dom.events.MouseEvent
import kotlin.browser.document
import kotlin.browser.window

class JsUi : IUi {
    abstract class JsUiNode(var element: HTMLElement? = null) : IUiNode {
        fun create(tag: String, id: String?) {
            check(element == null)
            element = document.createElement(tag) as HTMLElement
            if (id != null) {
                element!!.id = id
            }
        }

        override val tag get() = element!!.tagName
        override val id get() = element!!.id

        override val children: List<IUiNode>
            get() {
                var list: List<IUiNode> = emptyList()
                var child = element!!.firstElementChild as HTMLElement?
                while (child != null) {
                    if (child.localName.toLowerCase() == "span") {
                        list += JsUiText(child)
                    } else {
                        list += JsUiElement(child)
                    }
                    child = child.nextElementSibling as HTMLElement
                }
                return list
            }

        override var classes: Set<String>
            get() = element!!.classList.asList().toSet()
            set(replacement) {
                val current = classes
                replacement.minus(current).forEach { element!!.classList.add(it) }
                current.minus(replacement).forEach { element!!.classList.remove(it) }
            }

        override fun set(name: String, value: String?) {
            element!!.setProperty(name, value)
        }

        override fun get(name: String) = element!!.getProperty(name).toString()

        override fun remove() {
            element!!.parentElement!!.removeChild(element!!)
        }

        override fun scrollIntoView() {
            element!!.scrollIntoView()
        }

        override fun getPosition(): Pair<Int, Int> {
            val computed = window.getComputedStyle(element!!.parentElement!!)
            return Pair(
                    element!!.offsetLeft - fromPixels(computed.borderLeftWidth),
                    element!!.offsetTop - fromPixels(computed.borderTopWidth))
        }

        override fun setPosition(x: Int, y: Int) {
            element!!.style.left = "${x}px"
            element!!.style.top = "${y}px"
        }

        override fun getSize() = Pair(element!!.clientWidth, element!!.clientHeight)

        override fun setSize(x: Int, y: Int) {
            element!!.style.width = "${x}px"
            element!!.style.height = "${y}px"
        }

        override fun focus() = element!!.focus()

        override fun onDrag(callbacks: UiDragCallbacks) {
            element!!.onmousedown = {
                run {
                    val me = it as MouseEvent
                    me.preventDefault()
                    callbacks.onStart(me.clientX, me.clientY)
                    kickScheduler()
                }

                document.onmousemove = {
                    val me = it as MouseEvent
                    me.preventDefault()
                    callbacks.onMove(me.clientX, me.clientY)
                    kickScheduler()

                    false
                }

                document.onmouseup = {
                    val me = it as MouseEvent
                    me.preventDefault()
                    callbacks.onEnd(me.clientX, me.clientY)
                    document.onmousemove = null
                    document.onmouseup = null
                    kickScheduler()

                    false
                }

                false
            }
        }
    }

    class JsUiText(element: HTMLElement? = null) : IUiText, JsUiNode(element) {
        override var text: String
            get() = element!!.textContent!!
            set(value) {
                element!!.textContent = value
            }
    }

    class JsUiElement(element: HTMLElement? = null) : IUiElement, JsUiNode(element) {
        override fun addElement(tag: String, id: String?, init: UiElementConstructor): IUiElement {
            val e = JsUiElement()
            e.create(tag, id)
            e.init()
            appendChild(e)
            return e
        }

        override fun addText(tag: String, id: String?, text: String, init: UiTextConstructor): IUiText {
            val t = JsUiText()
            t.create(tag, id)
            t.text = text
            t.init()
            appendChild(t)
            return t
        }


        override fun appendChild(child: IUiNode): IUiElement {
            element!!.appendChild((child as JsUiNode).element!!)
            return this
        }

        override fun removeChild(child: IUiNode): IUiElement {
            element!!.removeChild((child as JsUiNode).element!!)
            return this
        }

        override fun onActivate(callback: () -> Unit) {
            var wrappedCallback = {
                callback()
                kickScheduler()
            }

            fun addClick() {
                element!!.onclick = { wrappedCallback() }
            }

            fun addReturn() {
                element!!.onkeyup = {
                    var k = it as KeyboardEvent
                    if ((document.activeElement == element!!) && (k.keyCode == 13)) {
                        wrappedCallback()
                    }
                }
            }

            when (tag) {
                "BUTTON" -> {
                    addClick()
                    addReturn()
                }

                "INPUT"  -> {
                    addReturn()
                }
            }
        }

        override fun activate() {
            element!!.click()
        }
    }

    override fun newModal(init: IUiElement.() -> Unit): IUiElement {
        val element = JsUiElement()
        element.create("div", null)
        element.init()
        document.body!!.appendChild(element.element!!)
        return element
    }
}

fun HTMLElement.getProperty(name: String): dynamic =
        this.asDynamic()[name]

fun <T : HTMLElement> T.setProperty(name: String, value: dynamic): T {
    this.asDynamic()[name] = value
    return this
}

private fun fromPixels(s: String): Int = s.removeSuffix("px").toInt()