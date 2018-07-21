package runtime.jvm

import interfaces.IUi
import interfaces.IUiElement
import interfaces.IUiNode
import interfaces.IUiText

class JvmStubUi: IUi {
    abstract class JvmUiNode(override val tag: String, override val id: String?): IUiNode {
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
    }

    class JvmUiText(tag: String, id: String? = null, override var text: String) : IUiText, JvmUiNode(tag, id)

    class JvmUiElement(tag: String, id: String? = null): IUiElement, JvmUiNode(tag, id) {
        private var onActivateCallback: (() -> Unit)? = null

        override fun addElement(tag: String, id: String?, init: IUiElement.() -> Unit): IUiElement {
            val e = JvmUiElement(tag, id)
            init(e)
            appendChild(e)
            return e
        }

        override fun addText(tag: String, id: String?, text: String): IUiText {
            val t = JvmUiText(tag, id, text)
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
        init(element)
        return element
    }
}
