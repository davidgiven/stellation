package ui;

import interfaces.IUi;
import model.Properties.NAME;
import model.SPlayer;
import utils.Injectomatic.inject;
using ui.UiTools;

@:tink
class SummaryWindow extends AbstractWindow {
	var player = inject(SPlayer);

	public function new() {
		super();
		mainClass = "summaryWindow";
		isResizable = false;
		layout = "right-to-left";
	}

	public override function createTitlebar(div: IUiElement) {
		div.addNode(
			ui.newText("span", "Summary")
		);
	}

	public override function createUserInterface(div: IUiElement) {
		div.addClasses(["scrollable"])
			.addNode(
				ui.newTimeViewer()
			)
			.addNode(
				ui.newStringViewer(player, NAME)
			);
	}
}

