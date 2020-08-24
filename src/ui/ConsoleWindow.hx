package ui;

import interfaces.IUi;

typedef ConsoleCommandCallback = (String) -> Void;

@:tink
class ConsoleWindow extends AbstractWindow {
	@:signal public var commandReceived: String;

	public function new() super();
}

//package ui
//
//import interfaces.IUiElement
//
//typealias ConsoleCommandCallback = (String) -> Unit
//
//class ConsoleWindow(val callback: ConsoleCommandCallback) : AbstractWindow() {
//    lateinit var linesBox: IUiElement
//    lateinit var textInput: IUiElement
//
//    override val mainClass = "consoleWindow"
//    override val isResizable = false
//
//    override fun createTitlebar(div: IUiElement) {
//        div.addText("span", "Console")
//    }
//
//    override fun createUserInterface(div: IUiElement) {
//        div.run {
//            classes += "scrollable"
//
//            addElement("div") {
//                classes += setOf("console")
//
//                addElement("label") {
//                    classes += setOf("expand")
//
//                    linesBox = addElement("div") {
//                        classes += setOf("lines")
//                    }
//
//                    addHBox {
//                        addText("div", ">") {
//                            classes += "prompt"
//                        }
//
//                        textInput = addElement("input") {
//                            set("type", "text")
//
//                            onActivate {
//                                val value = textInput["value"]!!
//                                textInput["value"] = ""
//                                callback(value)
//                            }
//                        }
//                    }
//
//                    addElement("div") {
//                        classes += "expand"
//                    }
//                }
//            }
//        }
//    }
//
//    fun print(s: String) {
//        linesBox.addText("div", s).classes
//        textInput.scrollIntoView()
//    }
//}
//
//
