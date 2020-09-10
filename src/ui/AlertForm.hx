package ui;

import interfaces.IUi;
import tink.CoreApi;

@:tink
@await
class AlertForm extends AbstractForm<Noise> {
	var title: String;
	var message: String;

	public function new(title: String, message: String) {
		super();

		this.title = title;
		this.message = message;
		this.mainClass = "alertWindow";
	}

    override function createTitlebar(div: IUiElement) {
		div.addNode(
			ui.newText("span", title)
		);
    }

    override function createUserInterface(div: IUiElement) {
		div
			.addClasses(["message"])
			.addNode(
				ui.newText("p", message)
					.addClasses(["wide-text", "centred-text"])
			);
	}

	override function createButtonBox(div: IUiElement) {
		createOkButtonBox(div, "OK").onActivate().handle(n -> onCompletionTrigger.trigger(Noise));
	}
}


