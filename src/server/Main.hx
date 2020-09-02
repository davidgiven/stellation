package server;
import sys.db.Sqlite;
import interfaces.IClock;
import interfaces.ITime;
import interfaces.IDatastore;
import runtime.shared.ServerClock;
import runtime.shared.Time;
//import runtime.shared.SqlDatastore;
import model.ObjectLoader;
import utils.Injectomatic.bind;
import utils.Injectomatic.inject;

class Main {
	static public function main(): Void {
		bind(IClock, new ServerClock());
		bind(ITime, new Time());

        //bind(IDatastore, new SqlDatastore());
        bind(ObjectLoader, new ObjectLoader());

		var db = Sqlite.open("fnord.sqlite");

		if (Sys.getEnv("GATEWAY_INTERFACE") != null) {
			Sys.println("cgi invocation");
		} else {
            new CliHandler().main(Sys.args());
		}
	}
}

// vim: ts=4 sw=4 et

