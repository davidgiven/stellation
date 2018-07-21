package interfaces

typealias UiConstructor = IUiElement.() -> Unit

interface IUiNode {
    val tag: String
    val id: String?
    val children: List<IUiNode>
    var classes: Set<String>

    operator fun set(name: String, value: String?)
    operator fun get(name: String): String?

    fun clear(name: String) = set(name, null)
}

interface IUiText : IUiNode {
    var text: String
}

interface IUiElement : IUiNode {
    fun onActivate(callback: () -> Unit)

    fun addElement(tag: String, id: String?, init: UiConstructor = {}): IUiElement
    fun addElement(tag: String, init: UiConstructor = {}): IUiElement = addElement(tag, null, init)

    fun addText(tag: String, id: String?, text: String): IUiText
    fun addText(tag: String, text: String): IUiText = addText(tag, null, text)

    fun appendChild(child: IUiNode): IUiElement
    fun removeChild(child: IUiNode): IUiElement
    fun activate()

    fun addVBox(init: UiConstructor) = addElement("div") {
        classes = setOf("vbox")
        init(this)
    }
}


interface IUi {
    fun newModal(init: IUiElement.() -> Unit): IUiElement
}
