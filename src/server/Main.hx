package server;
import sys.db.Sqlite;
import model.ObjectLoader;
import utils.Injectomatic.bind;
import utils.Injectomatic.inject;

class Main {
	static public function main(): Void {
		if (Sys.getEnv("GATEWAY_INTERFACE") != null) {
			Sys.println("cgi invocation");
		} else {
            new CliHandler().main(Sys.args());
		}
	}
}

// vim: ts=4 sw=4 et

