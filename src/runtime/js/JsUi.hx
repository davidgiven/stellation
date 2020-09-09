package runtime.js;

import interfaces.IUi;
import js.Browser;
import js.html.*;
import tink.CoreApi;
using Lambda;
using StringTools;
using utils.ArrayTools;

@:tink
class JsUiNode implements IUiNode {
	public var element: Element = _;
	public var onClickTrigger: SignalTrigger<Noise> = Signal.trigger();

	public function remove(): Void {
		element.parentElement.removeChild(element);
	}

	public function scrollIntoView(): Void {
        element.scrollIntoView();
	}

	public function getPosition(): UiPoint {
		var computed = Browser.window.getComputedStyle(element.parentElement);
		return {
			x: element.offsetLeft - fromPixels(computed.borderLeftWidth),
			y: element.offsetTop - fromPixels(computed.borderTopWidth)
		};
	}

	public function setPosition(x: Float, y: Float): Void {
		element.style.left = '${x}px';
		element.style.top = '${y}px';
	}

	public function getSize(): UiPoint {
		return {
			x: element.clientWidth,
			y: element.clientHeight
		};
	}

	public function setSize(x: Float, y: Float): Void {
		element.style.width = '${x}px';
		element.style.height = '${y}px';
	}

	public function onDrag(callbacks: UiDragCallbacks): Void {
		element.onmousedown = (it) -> {
			var me = cast(it, MouseEvent);
			me.preventDefault();
			callbacks.onStart(me.clientX, me.clientY);

			Browser.document.onmousemove = (it) -> {
				var me = cast(it, MouseEvent);
				me.preventDefault();
				callbacks.onMove(me.clientX, me.clientY);
				return false;
			};

			Browser.document.onmouseup = (it) -> {
				var me = cast(it, MouseEvent);
				me.preventDefault();
				callbacks.onEnd(me.clientX, me.clientY);
				Browser.document.onmousemove = null;
				Browser.document.onmouseup = null;
				return false;
			};
		};
	}

	public function onClick(): Signal<Noise> {
		element.onclick = it -> onClickTrigger.trigger(Noise);
		return onClickTrigger.asSignal();
	}

	private static function fromPixels(s: String): Float {
		return Std.parseFloat(s.replace("px", ""));
	}
}

@:tink
class JsUiElement extends JsUiNode implements IUiElement {
	@:calc var tag = element.tagName;
	@:calc var id = element.id;

	public function setAttr(name: String, value: String): IUiElement {
		element.setAttribute(name, value);
		return this;
	}

	public function getAttr(name: String): Null<String> {
		return element.getAttribute(name);
	}

	public function getClasses(): Iterable<String> {
		var s = element.getAttribute("class") | if (null) "";
		return s.split(" ");
	}

	public function setClasses(classes: Iterable<String>): IUiElement {
		element.setAttribute("class", classes.list().join(" "));
		return this;
	}

	public function addClasses(classes: Iterable<String>): IUiElement {
		var s = element.getAttribute("class") | if (null) "";
		element.setAttribute("class", s + " " + classes.list().join(" "));
		return this;
	}

	public function removeClasses(classes: Iterable<String>): IUiElement {
		var s = element.getAttribute("class") | if (null) "";
		var ss = s.split(" ").toMap();
		for (c in classes) {
			ss.remove(c);
		}
		element.setAttribute("class", ss.keys().join(" "));
		return this;
	}

	public function addNode(child: IUiNode): IUiElement {
		var c = cast(child, JsUiNode);
		element.appendChild(c.element);
		return this;
	}

	public function getValue(): String {
		return cast(element, InputElement).value;
	}

	public function setValue(value: String): IUiElement {
		cast(element, InputElement).value = value;
		return this;
	}

	public function hide(): IUiElement {
		addClasses(["hide"]);
		return this;
	}

	public function show(): IUiElement {
		removeClasses(["hide"]);
		return this;
	}

	public function focus(): IUiElement {
		element.focus();
		return this;
	}

	public function onActivate(callback: (Dynamic) -> Void): IUiElement {
		function addClick() {
			element.onclick = callback;
		}

		function addReturn() {
			element.onkeyup = (it) -> {
				var k = cast(it, KeyboardEvent);
				if ((Browser.document.activeElement == element) && (k.keyCode == 13)) {
					callback(it);
				}
			}
		}

		switch (tag) {
			case "BUTTON":
				addClick();
				addReturn();

			case "INPUT":
				addReturn();
		}

		return this;
	}
}

@:tink
class JsUiText extends JsUiElement implements IUiText {
	public override function getValue(): String {
		return element.textContent;
	}

	public override function setValue(value: String): IUiElement {
		element.textContent = value;
		return this;
	}
}

class JsUi implements IUi {
	public function new() {}

	public function newElement(tag: String): JsUiElement {
		var e = Browser.document.createElement(tag);
		return new JsUiElement(e);
	}

	public function newText(tag: String, text: String): JsUiText {
		var e = Browser.document.createElement(tag);
		e.textContent = text;
		return new JsUiText(e);
	}

	public function show(element: IUiElement): Void {
		var e = cast(element, JsUiElement);
		Browser.document.body.appendChild(e.element);
	}
}

