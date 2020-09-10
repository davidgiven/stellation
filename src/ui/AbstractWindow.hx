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

	public function create(): AbstractWindow {
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
			setResizeCallbacks(resizer.onDrag());
		}

		setDragCallbacks(titlebar.onDrag());

		element.onResize().handle(n -> _onGeometryChange.trigger(Noise));
		_onGeometryChange.trigger(Noise);
		return this;
	}

	public function getSize(): UiPoint {
		return element.getSize();
	}

	public function setSize(w: Float, h: Float): Void {
		element.setSize(w, h);
	}

	public function getPosition(): UiPoint {
		return element.getPosition();
	}

	public function setPosition(x: Float, y: Float): Void {
		element.setPosition(x, y);
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

	private function setDragCallbacks(cbs: UiDragCallbacks): Void {
		var x = 0.0;
		var y = 0.0;

		cbs.onStart.handle(p -> {
            var winPos = element.getPosition();
            x = winPos.x - p.x;
            y = winPos.y - p.y;
		});

		var move_cb = p -> {
			var newX = x + p.x;
			var newY = y + p.y;
			element.setPosition(newX, newY);
			_onGeometryChange.trigger(Noise);
		};

		cbs.onMove.handle(move_cb);
		cbs.onEnd.handle(move_cb);
	};

	private function setResizeCallbacks(cbs: UiDragCallbacks): Void {
		var x = 0.0;
		var y = 0.0;

		cbs.onStart.handle(p -> {
            var winSize = element.getSize();
            x = winSize.x - p.x;
            y = winSize.y - p.y;
		});

		var move_cb = p -> {
			var newW = x + p.x;
			var newH = x + p.y;
			element.setSize(newW, newH);
			_onGeometryChange.trigger(Noise);
		};

		cbs.onMove.handle(move_cb);
		cbs.onEnd.handle(move_cb);
	};
}

