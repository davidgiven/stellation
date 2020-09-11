package client;

import interfaces.IUi;
import runtime.js.JsUi;
import model.SGalaxy;
import model.SPlayer;
import model.SStar;
import model.Properties.CONTENTS;
import utils.Injectomatic.inject;
import js.Browser;
import js.html.*;
import tink.CoreApi;
import client.Images;
import interfaces.ILogger.Logger.log;
import Math;
using Math;
using StringTools;

@:tink
class MapViewer extends JsUiElement {
	var ui = inject(IUi);
	var galaxy = inject(SGalaxy);
	var player = inject(SPlayer);
	var width = 0.0;
	var height = 0.0;
	var deltaX = 0.0;
	var deltaY = 0.0;
	var scale = 10.0;
	var starRadius = 4.0;

	public function new() {
		super(Browser.document.createElement("canvas"));
		setClasses(["map"]);
		onResize().handle(n -> redraw());
		setDragCallbacks(onDrag());
		onWheel().handle(ev -> resize(ev));
		onClick().handle(ev -> click(ev));

		galaxy.onPropertyChanged(CONTENTS).handle(n -> redraw());
	}

	public function redraw() {
		var canvas = cast(element, CanvasElement);
		width = canvas.width = Browser.window.innerWidth;
		height = canvas.height = Browser.window.innerHeight;
		var ctx: CanvasRenderingContext2D = canvas.getContext("2d", { alpha: false });
		ctx.fillStyle = "#000";
		ctx.fillRect(0, 0, width, height);
		ctx.translate(width/2 + deltaX, height/2 + deltaY);

		var galaxyImage = Images.get(ImageId.GALAXY);
		var gw = SGalaxy.RADIUS*1.3*scale;
		ctx.drawImage(galaxyImage, -gw, -gw, gw*2, gw*2);

		var textAlpha = (scale - 10.0) / 20.0;
		if (textAlpha < 0.0) {
			textAlpha = 0;0;
		}
		if (textAlpha > 1.0) {
			textAlpha = 1.0;
		}

		ctx.strokeStyle = "#fff";
		ctx.fillStyle = "#fff" + Std.int(textAlpha*15.0).hex();
		ctx.font = "8px CommodorePet";
		for (o in galaxy.contents.getAll()) {
			var star = cast(o, SStar);
			var x = star.x*scale;
			var y = star.y*scale;
			ctx.beginPath();
			ctx.moveTo(x+starRadius, y);
			ctx.arc(x, y, starRadius, 0, 2*Math.PI);
			ctx.stroke();

			if (textAlpha > 0.0) {
				var starName = star.name.toUpperCase();
				var metrics = ctx.measureText(starName);
				ctx.fillText(starName, x - metrics.width/2, y+12);
			}
		}
	}

	private function resize(event: UiWheelEvent) {
		var amount = event.deltaY.abs() / 40.0;
		if (event.deltaY > 0.0) {
			amount = 1.0 / amount;
		}

		var mouseG = gcoords(event.mouseX, event.mouseY);

		/* Put the mouse position at the centre of the map. */

		deltaX += mouseG.x * scale;
		deltaY += mouseG.y * scale;

		scale *= amount;

		/* Now put things back the way they were. */

		deltaX -= mouseG.x * scale;
		deltaY -= mouseG.y * scale;

		Browser.window.requestAnimationFrame(ts -> redraw());
	}

	public function click(event: UiClickEvent) {
		var gc = gcoords(event.mouseX, event.mouseY);
		log('(${event.mouseX}, ${event.mouseY}) -> (${gc.x}, ${gc.y})');
	}

	private function setDragCallbacks(cbs: UiDragCallbacks): Void {
		var x = 0.0;
		var y = 0.0;

		cbs.onStart.handle(p -> {
            x = deltaX - p.x;
            y = deltaY - p.y;
		});

		var move_cb = p -> {
			deltaX = x + p.x;
			deltaY = y + p.y;
			Browser.window.requestAnimationFrame(ts -> redraw());
		};

		cbs.onMove.handle(move_cb);
		cbs.onEnd.handle(move_cb);
	};

	/* Convert screen coordinates to galactic coordinates. */
	private function gcoords(x: Float, y: Float): {x: Float, y: Float} {
		return {
			x: (x - width/2 - deltaX) / scale,
			y: (y - height/2 - deltaY) / scale
		};
	}

	/* Convert galactic coordinates to screen coordinates. */
	private function scoords(x: Float, y: Float): {x: Float, y: Float} {
		return {
			x: x * scale + deltaX + width/2,
			y: y * scale + deltaY + height/2
		};
	}
}

