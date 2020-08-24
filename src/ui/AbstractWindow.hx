package ui;

import utils.Injectomatic.inject;
import interfaces.IUi;

@:tink
class AbstractWindow {
	@:lazy var ui: IUi = inject(IUi);

	var element: IUiElement;
	var titlebar: IUiElement;

	public function create() {
		element = ui.newElement("div")
			.addClasses(["window"])
			.addNode(
				titlebar = ui.newElement("div")
					.setClasses(["titlebar"])
					.addNode(
						ui.newElement("span")
							.setClasses(["expand", "textured"])
					)
					.addNode(
						ui.newText("This is a title bar!")
					)
			)
			.addNode(
				ui.newElement("div")
					.setClasses(["body", "expand", "vbox"])
			);
		ui.show(element);

//
//
//        element = ui.newModal {
//            classes = setOf("window", layout, mainClass)
//
//            titlebar = addElement("div") {
//                classes = setOf("titlebar")
//                createTitlebar(this)
//
//                addElement("span") {
//                    classes = setOf("expand", "textured")
//                }
//
//                onDrag(dragCallbacks)
//            }
//
//            addElement("div") {
//                classes = setOf("body", "expand", "vbox")
//                createUserInterface(this)
//            }
//
//            if (isResizable) {
//                addElement("div") {
//                    classes = setOf("resizer")
//
//                    onDrag(resizeCallbacks)
//                }
//            }
//        }
	}
}

