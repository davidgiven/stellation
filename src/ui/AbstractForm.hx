package ui;

import interfaces.IUi;
import tink.CoreApi;

typedef YesNoButtons = { yes: IUiElement, no: IUiElement };

@await
class AbstractForm<T> extends AbstractWindow {
	var onCompletionTrigger: SignalTrigger<T>;
	var onCompletion: Signal<T>;

	public function new() {
		super();
		onCompletionTrigger = Signal.trigger();
		onCompletion = onCompletionTrigger.asSignal();
	}

	override public function create(): Void {
		super.create();
		element.addClasses(["form"]);

		var buttonbox;
		element.addNode(
			buttonbox = ui.newElement("div")
				.setClasses(["buttonbox"])
		);
		createButtonBox(buttonbox);
	}

	public function createButtonBox(div: IUiElement): Void {
		div.addNode(
			ui.newElement("button")
				.addClasses(["button", "defaultbutton"])
				.addNode(
					ui.newText("span", "OK")
				)
		);
	}

	public function createYesNoButtonBox(div: IUiElement, yes: String = "Yes", no: String = "No"): YesNoButtons {
		var yesButton;
		var noButton;

		div.addNode(
			yesButton = ui.newElement("button")
				.addClasses(["button", "defaultbutton"])
				.addNode(
					ui.newText("span", yes)
				)
		);
		div.addNode(
			noButton = ui.newElement("button")
				.addClasses(["button", "cancelbutton"])
				.addNode(
					ui.newText("span", no)
				)
		);

		return { yes: yesButton, no: noButton };
    }

	@async
	public function execute(): T {
		create();
		show();
		var result = @await onCompletion.next();
		hide();
		return result;
	}
}

