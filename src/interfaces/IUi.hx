package interfaces;

import tink.CoreApi;

typedef UiDragCallbacks = {
	onStart: (clientX: Float, clientY: Float) -> Void,
	onMove: (clientX: Float, clientY: Float) -> Void,
	onEnd: (clientX: Float, clientY: Float) -> Void,
};

typedef UiPoint = {
	x: Float,
	y: Float
};

interface IUiNode {
	public function remove(): Void;

	public function getPosition(): UiPoint;
	public function setPosition(x: Float, y: Float): Void;

	public function getSize(): UiPoint;
	public function setSize(x: Float, y: Float): Void;

	public function scrollIntoView(): Void;

	public function onDrag(callbacks: UiDragCallbacks): Void;
}

interface IUiText extends IUiNode {
}

interface IUiElement extends IUiNode {
	public function setAttr(name: String, value: String): IUiElement;
	public function getAttr(name: String): Null<String>;

	public function getClasses(): Array<String>;
	public function addClasses(classes: Array<String>): IUiElement;
	public function setClasses(classes: Array<String>): IUiElement;

	public function addNode(element: IUiNode): IUiElement;

	public function getValue(): String;
	public function setValue(value: String): IUiElement;

	public function onActivate(callback: (Dynamic) -> Void): IUiElement;
}

@:tink
interface IUi {
	public function newElement(tag: String): IUiElement;
	public function newText(text: String): IUiText;
	public function show(element: IUiElement): Void;

	public function newHBox(): IUiElement return newElement("div").setClasses(["hbox"]);
	public function newVBox(): IUiElement return newElement("div").setClasses(["vbox"]);
}

