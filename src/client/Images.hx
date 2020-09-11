package client;

import tink.CoreApi;
import js.Browser;
import js.html.*;

enum Image {
	GALAXY;
}

@await
@:tink
class Images {
	private static var imageMap = [
		GALAXY => newImage("galaxy.png")
	];

	public static function get(image: Image): ImageElement {
		return imageMap.get(image);
	}

	@async
	public static function loadImages(): Noise {
		for (image => element in imageMap) {
			if (!element.complete) {
				@await Future.async(handler -> {
					element.onload = () -> handler(Success(Noise));
				});
			}
		}
		return Noise;
	}

	private static function newImage(src: String): ImageElement {
		var e: ImageElement = cast(Browser.document.createElement("IMG"));
		e.src = src;
		return e;
	}
}

