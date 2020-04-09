import sys.db.Sqlite;

class ServerMain {
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
	}
}

