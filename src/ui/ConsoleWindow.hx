package ui;

import interfaces.IUi;

typedef ConsoleCommandCallback = (String) -> Void;

@:tink
class ConsoleWindow extends AbstractWindow {
	@:signal public var commandReceived: String;

	private var linesBox: IUiElement;
	private var textInput: IUiElement;

	public function new() {
		super();
		isResizable = true;
	}

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

