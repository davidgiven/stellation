package ui;

import interfaces.IUi;

typedef ConsoleCommandCallback = (String) -> Void;

@:tink
class ConsoleWindow extends AbstractWindow {
	@:signal public var onCommandReceived: String;

	private var linesBox: IUiElement;
	private var textInput: IUiElement;

	public function new() {
		super();
		isResizable = true;
	}

	public override function createTitlebar(div: IUiElement) {
		div.addNode(
			ui.newText("span", "Console")
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
												ui.newText("span", ">")
											)
									)
									.addNode(
										textInput = ui.newElement("input")
											.addClasses(["expand"])
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
				_onCommandReceived.trigger(value);
			}
		);
	}

	public function println(s: String): Void {
		linesBox.addNode(
			ui.newText("div", s)
		);
        textInput.scrollIntoView();
	}
}

