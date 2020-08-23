package server;
import sys.db.Sqlite;
import interfaces.IClock;
import interfaces.ITime;
import runtime.cpp.CppRuntime.initCppRuntime;
import runtime.shared.ServerClock;
import runtime.shared.Time;
import utils.Injectomatic.bind;
import utils.Injectomatic.inject;
import utils.Types.Oid;
class Main {
	static public function main(): Void {
		initCppRuntime();

		bind(IClock, new ServerClock());
		bind(ITime, new Time());

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

		var a: Oid = 7;
		var s = inject(String);
		Console.info('${s}');
	}
}

// vim: ts=4 sw=4 et

