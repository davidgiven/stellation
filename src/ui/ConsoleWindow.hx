package ui;

import interfaces.IUi;
import tink.CoreApi;

typedef ConsoleCommandCallback = (String) -> Void;

@:tink
class ConsoleWindow extends AbstractWindow {
	private var linesBox: IUiElement;
	private var inputBox: IUiElement;
	private var textInput: IUiElement;
	private var busy: Bool;

	private var onCommandReceivedTrigger: SignalTrigger<String> = Signal.trigger();

	public function new() {
		super();
		mainClass = "consoleWindow";
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
								inputBox = ui.newHBox()
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

		textInput.onActivate().handle(n -> {
			if (!busy) {
				var value = textInput.getValue();
				textInput.setValue("");
				setBusy();
				onCommandReceivedTrigger.trigger(value);
			}
		});
		setReady();
	}

	public function onCommandReceived(): Signal<String> {
		return onCommandReceivedTrigger.asSignal();
	}

	public function println(s: String): Void {
		linesBox.addNode(
			ui.newText("div", s)
		);
        textInput.scrollIntoView();
	}

	public function setBusy(): Void {
		inputBox.hide();
		busy = true;
	}

	public function setReady(): Void {
		inputBox.show();
        textInput.scrollIntoView();
		busy = false;
	}
}

