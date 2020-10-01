package ui;

import interfaces.IUi;
import model.Properties.NAME;
import model.SShip;
using ui.UiTools;

class ShipWindow extends AbstractWindow {
	var ship: SShip;

	public function new(ship: SShip) {
		super();
		this.ship = ship;
		isResizeable = true;
		isCloseable = true;
	}

    override function createTitlebar(div: IUiElement) {
		div.addNode(
			ui.newStringViewer(ship, NAME)
		);
    }

	public override function createUserInterface(div: IUiElement) {
		div.addNode(
			ui.newElement("div")
				.addNode(
					ui.newTable([
						{ label: "Name:",        element: ui.newStringViewer(ship, NAME) },
						{ label: "Owner:",       element: ui.newStringViewer(ship.owner, NAME) },
						{ label: "Location:",    element: ui.newStarViewer(ship.getContainingStar()) },
					])
				)
		);
	}
}


