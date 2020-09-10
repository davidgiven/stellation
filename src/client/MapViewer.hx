package client;

import interfaces.IUi;
import runtime.js.JsUi;
import model.SGalaxy;
import model.SStar;
import model.Properties.CONTENTS;
import utils.Injectomatic.inject;
import js.Browser;
import js.html.*;
import tink.CoreApi;
import Math;

@:tink
class MapViewer extends JsUiElement {
	var ui = inject(IUi);
	var galaxy = inject(SGalaxy);

	public function new() {
		super(Browser.document.createElement("canvas"));
		setClasses(["map"]);
		onResize().handle(n -> redraw());

		galaxy.onPropertyChanged(CONTENTS).handle(n -> redraw());
	}

	public function redraw() {
		var canvas = cast(element, CanvasElement);
		var w = canvas.width = Browser.window.innerWidth;
		var h = canvas.height = Browser.window.innerHeight;
		var ctx: CanvasRenderingContext2D = canvas.getContext("2d", { alpha: false });
		ctx.strokeStyle = "#fff";
		ctx.beginPath();
		ctx.moveTo(0, 0);
		ctx.lineTo(w, h);
		ctx.moveTo(0, h);
		ctx.lineTo(w, 0);
		ctx.stroke();

		var scale = 10.0;
		var radius = 4.0;
		ctx.beginPath();
		for (o in galaxy.contents.getAll()) {
			var star = cast(o, SStar);
			var x = (w/2) + star.x*scale;
			var y = (h/2) + star.y*scale;
			ctx.moveTo(x+radius, y);
			ctx.arc(x, y, radius, 0, 2*Math.PI);
		}
		ctx.stroke();
	}
}

