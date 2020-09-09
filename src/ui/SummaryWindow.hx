package ui;

import interfaces.IUi;
import model.Properties.NAME;
import model.Properties.SHIPS;
import model.SPlayer;
import model.SShip;
import utils.Injectomatic.inject;
using ui.UiTools;

@:tink
class SummaryWindow extends AbstractWindow {
	var player = inject(SPlayer);
	var shipList: IUiElement;

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
				ui.newElement("div")
					.addNode(
						ui.newCurrentTimeViewer()
					)
					.addNode(
						ui.newStringViewer(player, NAME)
					)
					.addNode(
						ui.newSeparator("SHIPS")
					)
					.addNode(
						shipList = ui.newElement("div")
					)
			);

		var shipViewers: Map<SShip, IUiElement> = [];
		var update_ships_cb = n -> {
			var updatedShips = player.ships;
			for (ship => element in shipViewers) {
				if (!updatedShips.exists(ship)) {
					element.remove();
					shipViewers.remove(ship);
				}
			}
			for (ship in updatedShips.getAll()) {
				if (!shipViewers.exists(ship)) {
					var element = newShipViewer(ship);
					shipList.addNode(element);
					shipViewers[ship] = element;
				}
			}
		};
		player.onPropertyChanged(SHIPS).handle(update_ships_cb);
		update_ships_cb(Noise);
	}

	private function newShipViewer(ship: SShip): IUiElement {
		var element = ui.newElement("div").addClasses(["ship-summary"]);
		element.addNode(
			ui.newStringViewer(ship, NAME)
		).addNode(
			ui.newText("span", " at ")
		).addNode(
			ui.newStringViewer(ship.getContainingStar(), NAME)
		);
		return element;
	}
}

