package runtime.jvm

import interfaces.IUi
import interfaces.IUiElement
import interfaces.IUiNode
import interfaces.IUiText
import interfaces.UiDragCallbacks
import interfaces.UiElementConstructor
import interfaces.UiTextConstructor

class JvmStubUi : IUi {
    abstract class JvmUiNode(override val tag: String, override val id: String?) : IUiNode {
        override var children: List<IUiNode> = emptyList()
        override var classes: Set<String> = emptySet()

        private var attributes: Map<String, String> = emptyMap()

        override fun set(name: String, value: String?) {
            if (value == null) {
                attributes -= name
            } else {
                attributes += name to value
            }
        }

        override fun get(name: String) = attributes.get(name)

        override fun remove() {}
        override fun scrollIntoView() {}
        override fun getPosition(): Pair<Int, Int> = Pair(0, 0)
        override fun setPosition(x: Int, y: Int) {}
        override fun getSize(): Pair<Int, Int> = Pair(0, 0)
        override fun setSize(x: Int, y: Int) {}
        override fun focus() {}

        override fun onDrag(callbacks: UiDragCallbacks) {}
        override fun onGlobalEvent(event: String, callback: () -> Unit) {}
    }

    class JvmUiText(tag: String, id: String? = null, override var text: String) : IUiText, JvmUiNode(tag, id)

    class JvmUiElement(tag: String, id: String? = null) : IUiElement, JvmUiNode(tag, id) {
        private var onActivateCallback: (() -> Unit)? = null

        override fun addElement(tag: String, id: String?, init: UiElementConstructor): IUiElement {
            val e = JvmUiElement(tag, id)
            e.init()
            appendChild(e)
            return e
        }

        override fun addText(tag: String, id: String?, text: String, init: UiTextConstructor): IUiText {
            val t = JvmUiText(tag, id, text)
            t.init()
            appendChild(t)
            return t
        }


        override fun appendChild(child: IUiNode): IUiElement {
            children += child as JvmUiNode
            return this
        }

        override fun removeChild(child: IUiNode): IUiElement {
            children -= child as JvmUiNode
            return this
        }

        override fun onActivate(callback: () -> Unit) {
            onActivateCallback = callback
        }

        override fun activate() {
            onActivateCallback!!()
        }
    }

    override fun newModal(init: IUiElement.() -> Unit): IUiElement {
        val element = JvmUiElement("div")
        element.init()
        return element
    }

    override fun fireGlobalEvent(event: String) {}
}
