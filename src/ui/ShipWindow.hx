package ui;

import interfaces.IUi;
import model.Properties.NAME;
import model.Properties.CONTENTS;
import model.Properties.KIND;
import model.SThing;
import model.SModule;
import model.SShip;
import tink.CoreApi;
using ui.UiTools;

class ShipWindow extends AbstractWindow {
	var ship: SShip;
	var moduleList: IUiElement;

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
				.addNode(
					moduleList = ui.newElement("div")
				)
		);

		var moduleViewers: Map<SThing, IUiElement> = [];
		var update_modules_cb = n -> {
			var updatedModules = ship.contents;
			for (module => element in moduleViewers) {
				if (!updatedModules.exists(module)) {
					element.remove();
					moduleViewers.remove(module);
				}
			}
			for (module in updatedModules.getAll()) {
				if (!moduleViewers.exists(module)) {
					var element = newModuleViewer(cast(module, SModule));
					moduleList.addNode(element);
					moduleViewers[module] = element;
				}
			}
		};
		ship.onPropertyChanged(CONTENTS).handle(update_modules_cb);
		update_modules_cb(Noise);
	}

	private function newModuleViewer(module: SModule): IUiElement {
		var element = ui.newElement("div").addClasses(["module-summary"]);
		element.addNode(
			ui.newStringViewer(module, KIND)
		);
		return element;
	}
}


