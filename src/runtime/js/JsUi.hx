package runtime.js;

import interfaces.IUi;
import js.Browser;
import js.html.*;

@:tink
class JsUiNode implements IUiNode {
	public var element: Element = _;
}

@:tink
class JsUiText extends JsUiNode implements IUiText {
}

@:tink
class JsUiElement extends JsUiNode implements IUiElement {
	public function getClasses(): Array<String> {
		var s = element.getAttribute("class") | if (null) "";
		return s.split(" ");
	}

	public function setClasses(classes: Array<String>): IUiElement {
		element.setAttribute("class", classes.join(" "));
		return this;
	}

	public function addClasses(classes: Array<String>): IUiElement {
		var s = element.getAttribute("class") | if (null) "";
		element.setAttribute("class", s + " " + classes.join(" "));
		return this;
	}

	public function addNode(child: IUiNode): IUiElement {
		var c = cast(child, JsUiNode);
		element.appendChild(c.element);
		return this;
	}
}

class JsUi implements IUi {
	public function new() {}

	public function newElement(tag: String): JsUiElement {
		var e = Browser.document.createElement(tag);
		return new JsUiElement(e);
	}

	public function newText(text: String): JsUiText {
		var e = Browser.document.createElement("span");
		e.textContent = text;
		return new JsUiText(e);
	}

	public function show(element: IUiElement): Void {
		var e = cast(element, JsUiElement);
		Browser.document.body.appendChild(e.element);
	}
}

