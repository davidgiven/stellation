package ui;

import utils.Injectomatic.inject;
import interfaces.IUi;
import tink.CoreApi;

@:tink
class AbstractWindow {
	@:lazy var ui: IUi = inject(IUi);

	@:signal public var onGeometryChange: Noise;

	var mainClass = @byDefault "";
    var layout = @byDefault "top-to-bottom";

	var element: IUiElement;
	var titlebar: IUiElement;
	var body: IUiElement;

	private var dragStartX: Float;
	private var dragStartY: Float;
	private var dragCallbacks: UiDragCallbacks = {
		onStart: (x, y) -> {
            var winPos = element.getPosition();
            dragStartX = winPos.x - x;
            dragStartY = winPos.y - y;
		},

		onMove: (x, y) -> {
			var newX = dragStartX + x;
			var newY = dragStartY + y;
			element.setPosition(newX, newY);
			_onGeometryChange.trigger(Noise);
		},

		onEnd: (x, y) -> dragCallbacks.onMove(x, y)
	};

	public function create() {
		element = ui.newElement("div")
			.addClasses(["window", layout, mainClass])
			.addNode(
				titlebar = ui.newElement("div")
					.setClasses(["titlebar"])
					.addNode(
						ui.newElement("span")
							.setClasses(["expand", "textured"])
					)
			)
			.addNode(
				body = ui.newElement("div")
					.setClasses(["body", "expand", "vbox"])
			);
		createTitlebar(titlebar);
		createUserInterface(body);

		titlebar.onDrag(dragCallbacks);
		ui.show(element);

		_onGeometryChange.trigger(Noise);
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

	function createTitlebar(div: IUiElement) {
		div.addNode(
			ui.newText(Type.getClassName(Type.getClass(this)))
		);
	}

	function createUserInterface(div: IUiElement) {
		div.addNode(
			ui.newText("Empty window")
		);
	}
}

