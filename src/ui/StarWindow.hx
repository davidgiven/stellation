package ui;

import interfaces.IUi;
import model.Properties.NAME;
import model.Properties.BRIGHTNESS;
import model.Properties.POSITION;
import model.Properties.ASTEROIDS;
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
						{ label: "Coordinates:", element: ui.newPositionViewer(star, POSITION) },
						{ label: "Asteroids:",   element: ui.newAsteroidsViewer(star, ASTEROIDS) },
					])
				)
		);
	}
}

