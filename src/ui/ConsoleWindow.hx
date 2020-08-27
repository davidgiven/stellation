package ui;

import interfaces.IUi;

typedef ConsoleCommandCallback = (String) -> Void;

@:tink
class ConsoleWindow extends AbstractWindow {
	@:signal public var commandReceived: String;

	private var linesBox: IUiElement;
	private var textInput: IUiElement;

	public function new() super();

	public override function createTitlebar(div: IUiElement) {
		div.addNode(
			ui.newText("Console")
		);
	}

	public override function createUserInterface(div: IUiElement) {
		div
			.addClasses(["scrollable"])
			.addNode(
				ui.newElement("div")
					.addClasses(["console"])
					.addNode(
						ui.newElement("label")
							.addClasses(["expand"])
							.addNode(
								linesBox = ui.newElement("div")
									.addClasses(["lines"])
							)
							.addNode(
								ui.newHBox()
									.addNode(
										ui.newElement("div")
											.addClasses(["prompt"])
											.addNode(
												ui.newText(">")
											)
									)
									.addNode(
										textInput = ui.newElement("input")
											.setAttr("type", "text")
									)
							)
							.addNode(
								ui.newElement("div")
									.setClasses(["expand"])
							)
					)
			);

		textInput.onActivate(
			(it) -> {
				var value = textInput.getValue();
				textInput.setValue("");
				trace(value);
				_commandReceived.trigger(value);
			}
		);
	}

	public function print(s: String): Void {
		linesBox.addNode(
			ui.newElement("div")
				.addNode(
					ui.newText(s)
			)
		);
        textInput.scrollIntoView();
	}
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
