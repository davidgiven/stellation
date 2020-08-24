package interfaces;

interface IUiNode {
}

interface IUiText extends IUiNode {
}

interface IUiElement extends IUiNode {
	public function getClasses(): Array<String>;
	public function addClasses(classes: Array<String>): IUiElement;
	public function setClasses(classes: Array<String>): IUiElement;

	public function addNode(element: IUiNode): IUiElement;
}

interface IUi {
	public function newElement(tag: String): IUiElement;
	public function newText(text: String): IUiText;
	public function show(element: IUiElement): Void;
}

