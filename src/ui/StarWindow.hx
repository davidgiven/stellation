package ui;

import interfaces.IUi;
import model.Properties.NAME;
import model.Properties.BRIGHTNESS;
import model.Properties.X;
import model.Properties.Y;
import model.Properties.ASTEROIDSM;
import model.Properties.ASTEROIDSC;
import model.SStar;
using ui.UiTools;

class StarWindow extends AbstractWindow {
	var star: SStar;

	public function new(star: SStar) {
		super();
		this.star = star;
		isResizeable = true;
		isCloseable = true;
	}

    override function createTitlebar(div: IUiElement) {
		div.addNode(
			ui.newText("span", star.name)
		);
    }

	public override function createUserInterface(div: IUiElement) {
		div.addNode(
			ui.newElement("div")
				.addNode(
					ui.newTable([
						{ label: "Name:",        element: ui.newStringViewer(star, NAME) },
						{ label: "Brightness:",  element: ui.newFloatViewer(star, BRIGHTNESS) },
						{ label: "Location:",    element: ui.newLocationViewer(star, X, Y) },
						{ label: "Asteroids:",   element: ui.newElement("span")
															.addNode(
																ui.newIntViewer(star, ASTEROIDSM)
															)
															.addText("span", "M, ")
															.addNode(
																ui.newIntViewer(star, ASTEROIDSC)
															)
															.addText("span", "C ")
															},
					])
				)
		);
	}
}

