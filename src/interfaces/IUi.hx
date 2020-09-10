package interfaces;

import tink.CoreApi;

typedef UiPoint = {
	x: Float,
	y: Float
};

typedef UiDragCallbacks = {
	onStart: Signal<UiPoint>,
	onMove: Signal<UiPoint>,
	onEnd: Signal<UiPoint>
};

interface IUiNode {
	public function remove(): Void;

	public function getPosition(): UiPoint;
	public function setPosition(x: Float, y: Float): Void;

	public function getSize(): UiPoint;
	public function setSize(x: Float, y: Float): Void;

	public function scrollIntoView(): Void;

	public function onDrag(): UiDragCallbacks;
	public function onClick(): Signal<Noise>;
	public function onResize(): Signal<Noise>;
}

interface IUiElement extends IUiNode {
	public function setAttr(name: String, value: String): IUiElement;
	public function getAttr(name: String): Null<String>;

	public function getClasses(): Iterable<String>;
	public function addClasses(classes: Iterable<String>): IUiElement;
	public function removeClasses(classes: Iterable<String>): IUiElement;
	public function setClasses(classes: Iterable<String>): IUiElement;

	public function addNode(element: IUiNode): IUiElement;

	public function getValue(): String;
	public function setValue(value: String): IUiElement;

	public function hide(): IUiElement;
	public function show(): IUiElement;
	public function focus(): IUiElement;

	public function onActivate(): Signal<Dynamic>;
}

interface IUiText extends IUiElement {
}

@:tink
interface IUi {
	public function newElement(tag: String): IUiElement;
	public function newText(tag: String, text: String): IUiText;
	public function show(element: IUiNode): Void;

	public function newHBox(): IUiElement return newElement("div").setClasses(["hbox"]);
	public function newVBox(): IUiElement return newElement("div").setClasses(["vbox"]);
}

