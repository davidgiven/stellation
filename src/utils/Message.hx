package utils;

import utils.Exception.throwInvalidCodecDataException;
using utils.NullTools;
using StringTools;

class Message {
	public static var LENGTH = "_count";

	private var _map = new Map<String, String>();
	public var length = 0;

	public static function deserialise(data: String): Message {
		var m = new Map<String, String>();
		for (line in ~/\n/g.split(data)) {
			var words = ~/=/.split(line);
			if ((words.length == 1) && (words[0] == "")) {
				break;
			}
			if (words.length != 2) {
				throwInvalidCodecDataException("couldn't parse line");
			}
			var key = decodeString(words[0]);
			var value = decodeString(words[1]);
			m[key] = value;
		}
		return new Message(m);
	}

	public function new(?map: Map<String, String>) {
		if (map != null) {
			_map = map;
			var s = _map[LENGTH];
			if (s != null) {
				length = Std.parseInt(s);
			}
		}
	}

	public function toMap(): Map<String, String> {
		if (length > 0) {
			_map[LENGTH] = Std.string(length);
		} else {
			_map.remove(LENGTH);
		}

		return _map;
	}

	public function toArray(): Array<String> {
		return [for (i in 0...length) getString(i)];
	}

    public function serialise(): String {
        var sb = new StringBuf();

        var map = toMap();
        var keys = [for (k => v in map) k];
		keys.sort((o1, o2) -> if (o1 < o2) -1 else if (o1 > o2) 1 else 0);
        for (k in keys) {
            var v = map[k];
            sb.add(encodeString(k));
            sb.add('=');
            sb.add(encodeString(v));
            sb.add('\n');
        }

        return sb.toString();
    }

	private static function encodeString(s: String): String {
		return s.replace("%", "%p").replace("\n", "%n").replace("=", "%e");
	}

	private static function decodeString(s: String): String {
		return s.replace("%e", "=").replace("%n", "\n").replace("%p", "%");
	}

	public function exists<T>(key: T) { return _map.exists(Std.string(key)); }

	public function getInt<T>(key: T): Null<Int>       return Std.parseInt(_map.get(Std.string(key)));
	public function getFloat<T>(key: T): Null<Float>   return Std.parseFloat(_map.get(Std.string(key)));
	public function getString<T>(key: T): Null<String> return _map.get(Std.string(key));
	public function getBool<T>(key: T): Null<Bool>     return _map.get(Std.string(key)).then(s -> (s == "1"));

	public function setInt<T>(key: T, value: Int): Message       { _map.set(Std.string(key), Std.string(value)); return this; }
	public function setFloat<T>(key: T, value: Float): Message   { _map.set(Std.string(key), Std.string(value)); return this; }
	public function setString<T>(key: T, value: String): Message { _map.set(Std.string(key), value); return this; }
	public function setBool<T>(key: T, value: Bool): Message     { _map.set(Std.string(key), value ? "1" : "0"); return this; }

	public function addString(value: String): Message return setString(length++, value);
}
