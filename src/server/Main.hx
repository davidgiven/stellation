package server;
import sys.db.Sqlite;
import utils.Injectomatic.inject;
import utils.Injectomatic.bind;

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
		var s = inject(String);
		Console.info('${s}');
	}
}

// vim: ts=4 sw=4 et

