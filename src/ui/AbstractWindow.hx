package ui;

import utils.Injectomatic.inject;
import interfaces.IUi;
import tink.CoreApi;

@:tink
class AbstractWindow {
	@:lazy var ui: IUi = inject(IUi);

	@:signal public var onGeometryChange: Noise;

	var isResizable = false;
	var mainClass = "";
    var layout = "top-to-bottom";

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

	private var resizeCallbacks: UiDragCallbacks = {
		onStart: (x, y) -> {
            var winSize = element.getSize();
            dragStartX = winSize.x - x;
            dragStartY = winSize.y - y;
		},

		onMove: (x, y) -> {
			var newW = dragStartX + x;
			var newH = dragStartY + y;
			element.setSize(newW, newH);
			_onGeometryChange.trigger(Noise);
		},

		onEnd: (x, y) -> resizeCallbacks.onMove(x, y)
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

		if (isResizable) {
			var resizer: IUiElement;
			element.addNode(
				resizer = ui.newElement("div")
					.setClasses(["resizer"])
			);
			resizer.onDrag(resizeCallbacks);
		}

		titlebar.onDrag(dragCallbacks);

		_onGeometryChange.trigger(Noise);
	}

	public function show(): AbstractWindow {
		ui.show(element);
		return this;
	}

	public function hide(): AbstractWindow {
		element.remove();
		return this;
	}

	function createTitlebar(div: IUiElement) {
		div.addNode(
			ui.newText("span", Type.getClassName(Type.getClass(this)))
		);
	}

	function createUserInterface(div: IUiElement) {
		div.addNode(
			ui.newText("div", "Empty window")
		);
	}
}

