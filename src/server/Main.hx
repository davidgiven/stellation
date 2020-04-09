package server;
import sys.db.Sqlite;
import haxe.ds.ObjectMap;

class Main {
	static public function main() {
		Sys.println("Hello, world!");
		var db = Sqlite.open("fnord.sqlite");

		if (Sys.getEnv("GATEWAY_INTERFACE") != null) {
			Sys.println("cgi invocation");
		} else {
			Sys.println("command line invocation");
		}

		Console.start();
		Console.info("info");

		bind(String, "fnord");

		var a = 7;
		var s = get(String);
		Console.info('${s}');
	}

	private static var injections = new ObjectMap<Dynamic, Dynamic>();

	@:generic
	static function get<T>(t: Class<T>): T {
		return injections.get(t);
	}

	@:generic
	static function bind<T>(t: Class<T>, value: T) {
		injections.set(t, value);
	}
}

