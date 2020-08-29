package client;

import js.Syntax;
import js.Browser;
using DateTools;
using StringTools;

class Cookies {
	private static final PREFIX = "com.cowlark.stellation.";

	public function new() {}

	public function get(name: String): Null<String> {
		var encodedName = PREFIX + encodeURIComponent(name);
		var pairs = ~/; */g.split(Browser.document.cookie);
		var template = '$encodedName=';
		for (pair in pairs) {
			if (pair.startsWith(template)) {
				var encodedValue = pair.substr(template.length);
				return decodeURIComponent(encodedValue);
			}
		}
        return null;
    }

	public inline function set(name: String, value: Null<String>): Cookies {
		var expiry = js.lib.Date.fromHaxeDate(Date.now().delta(1000.0 * 3600.0 * 24.0 * 364.0)).toUTCString();
		var encodedName = encodeURIComponent(name);
		var encodedValue = encodeURIComponent(value);

		Browser.document.cookie = '$PREFIX$encodedName=$encodedValue;expires=$expiry;path=/';
		return this;
	}

	private static function encodeURIComponent(s: String): String
		return Syntax.code("encodeURIComponent({0})", s);

	private static function decodeURIComponent(s: String): String
		return Syntax.code("decodeURIComponent({0})", s);
}

