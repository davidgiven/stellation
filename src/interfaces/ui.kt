package interfaces

import utils.Oid

typealias UiElementConstructor = IUiElement.() -> Unit
typealias UiTextConstructor = IUiText.() -> Unit

interface UiDragCallbacks {
    fun onStart(x: Int, y: Int)
    fun onMove(x: Int, y: Int)
    fun onEnd(x: Int, y: Int)
}

interface IUiNode {
    val tag: String
    val id: String?
    val children: List<IUiNode>
    var classes: Set<String>

    operator fun set(name: String, value: String?)
    operator fun get(name: String): String?

    fun clear(name: String) = set(name, null)

    fun remove()
    fun scrollIntoView()
    fun getPosition(): Pair<Int, Int>
    fun setPosition(x: Int, y: Int)
    fun getSize(): Pair<Int, Int>
    fun setSize(x: Int, y: Int)
    fun focus()

    fun onDrag(callbacks: UiDragCallbacks)
    fun onGlobalEvent(event: String, callback: () -> Unit)
}

interface IUiText : IUiNode {
    var text: String
}

interface IUiElement : IUiNode {
    fun onActivate(callback: () -> Unit)

    fun addElement(tag: String, id: String?, init: UiElementConstructor = {}): IUiElement
    fun addElement(tag: String, init: UiElementConstructor = {}): IUiElement = addElement(tag, null, init)

    fun addText(tag: String, id: String?, text: String, init: UiTextConstructor = {}): IUiText
    fun addText(tag: String, text: String, init: UiTextConstructor = {}): IUiText = addText(tag, null, text, init)

    fun appendChild(child: IUiNode): IUiElement
    fun removeChild(child: IUiNode): IUiElement
    fun activate()

    fun addVBox(init: UiElementConstructor) = addElement("div") {
        classes = setOf("vbox")
        init(this)
    }

    fun addHBox(init: UiElementConstructor) = addElement("div") {
        classes = setOf("hbox")
        init(this)
    }
}


interface IUi {
    fun newModal(init: IUiElement.() -> Unit): IUiElement

    fun fireGlobalEvent(event: String)
}

private fun makePropertyChangedEvent(oid: Oid, property: String) = "property-changed.$oid.$property"

private const val TICK_GLOBAL_EVENT = "tick"

fun IUiNode.onPropertyChangedGlobalEvent(oid: Oid, property: String, callback: () -> Unit) =
        onGlobalEvent(makePropertyChangedEvent(oid, property), callback)

fun IUi.firePropertyChangedGlobalEvent(oid: Oid, property: String) =
        fireGlobalEvent(makePropertyChangedEvent(oid, property))

fun IUiNode.onTickGlobalEvent(callback: ()->Unit) =
        onGlobalEvent(TICK_GLOBAL_EVENT, callback)

fun IUi.fireTickGlobalEvent() =
        fireGlobalEvent(TICK_GLOBAL_EVENT)
