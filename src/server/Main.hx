package server;
import sys.db.Sqlite;
import model.ObjectLoader;
import utils.Injectomatic.bind;
import utils.Injectomatic.inject;
import interfaces.ILogger;
import runtime.shared.FileLogger;

class Main {
	static public function main(): Void {
        bind(ILogger, new FileLogger(Configuration.LOGFILE_PATH));
		if (Sys.getEnv("GATEWAY_INTERFACE") != null) {
            new CgiHandler().main();
		} else {
            new CliHandler().main(Sys.args());
		}
	}
}

// vim: ts=4 sw=4 et

