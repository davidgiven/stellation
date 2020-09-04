package server;

import commands.CommandDispatcher;
import interfaces.IAuthenticator;
import interfaces.IClock;
import interfaces.IConsole;
import interfaces.IDatastore;
import interfaces.ITime;
import model.ObjectLoader;
import model.SUniverse;
import runtime.cpp.Console;
import runtime.cpp.SqlDatastore;
import runtime.cpp.Sqlite;
import runtime.shared.ServerClock;
import runtime.shared.Time;
import utils.Fault;
import utils.Injectomatic.bind;
import utils.Injectomatic.inject;
import utils.Random;

@:tink
class AbstractHandler {
    @:lazy var datastore = inject(IDatastore);
    @:lazy var objectLoader = inject(ObjectLoader);
	@:lazy var authenticator = inject(IAuthenticator);
	@:lazy var commandDispatcher = inject(CommandDispatcher);

    public function findUniverse(): SUniverse {
        return objectLoader.loadObject(1, SUniverse);
    }

    public function withServer(filename: String, callback: () -> Void): Void {
		bind(IClock, new ServerClock());
		bind(ITime, new Time());
		bind(Random, new Random());
		bind(IConsole, new Console());
		bind(IAuthenticator, new ServerAuthenticator());
        bind(CommandDispatcher, new CommandDispatcher());


		var database = Sqlite.open(filename);
		bind(SqliteDatabase, database);
        bind(IDatastore, new SqlDatastore());
        bind(ObjectLoader, new ObjectLoader());

        datastore.withTransaction(() -> {
            datastore.initialiseDatabase();
            objectLoader.initialiseProperties();
			authenticator.initialiseDatabase();
        });

        try {
            callback();
            database.close();
        } catch (f) {
			trace(f);
            database.close();
            throw f;
        }
    }
//fun withServer(dbfile: String, callback: () -> Unit) {
//    val database by injection<IDatabase>()
//    val datastore by injection<IDatastore>()
//    val timers by injection<Timers>()
//    database.openDatabase(dbfile)
//    val model by injection<Model>()
//    val auth by injection<IAuthenticator>()
//
//    database.withSqlTransaction {
//        datastore.initialiseDatabase()
//        model.initialiseProperties()
//        auth.initialiseDatabase()
//        timers.initialiseDatabase()
//    }
//
//    try {
//        callback()
//    } finally {
//        database.closeDatabase()
//    }
//}

}

