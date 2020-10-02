package server;

import commands.CommandDispatcher;
import interfaces.IAuthenticator;
import interfaces.IClock;
import interfaces.IConsole;
import interfaces.IDatastore;
import interfaces.ITimers;
import interfaces.ILogger.Logger.log;
import model.ObjectLoader;
import model.SPlayer;
import model.SUniverse;
import model.SThing;
import runtime.cpp.Console;
import runtime.cpp.SqlDatastore;
import runtime.cpp.Sqlite;
import runtime.shared.ServerClock;
import runtime.shared.Time;
import utils.Fault;
import utils.Injectomatic.bind;
import utils.Injectomatic.inject;
import utils.Random;
import haxe.Exception;

@:tink
class AbstractHandler {
    @:lazy var datastore = inject(IDatastore);
    @:lazy var objectLoader = inject(ObjectLoader);
	@:lazy var authenticator = inject(IAuthenticator);
	@:lazy var commandDispatcher = inject(CommandDispatcher);
	@:lazy var clock = inject(IClock);
	@:lazy var time = inject(Time);
	@:lazy var timers = inject(ITimers);

    public function findUniverse(): SUniverse {
        return objectLoader.findUniverse();
    }

    public function withServer(filename: String, callback: () -> Void): Void {
		bind(IClock, new ServerClock());
		bind(Time, new Time());
		bind(Random, new Random());
		bind(IConsole, new Console());
		bind(IAuthenticator, new ServerAuthenticator());
        bind(CommandDispatcher, new CommandDispatcher());

		clock.setTime(time.realtime());

		var database = Sqlite.open(filename);
		bind(SqliteDatabase, database);
		var datastore = new SqlDatastore();
        bind(IDatastore, datastore);
		bind(ITimers, datastore);
        bind(ObjectLoader, new ObjectLoader());

        datastore.withTransaction(() -> {
            datastore.initialiseDatabase();
            objectLoader.initialiseProperties();
			authenticator.initialiseDatabase();
        });

        try {
            callback();
            database.close();
        } catch (f: Fault) {
            database.close();
			throw f.rethrow();
        } catch (d: Dynamic) {
            database.close();
			throw d;
		}
    }

	public function catchup(): Void {
		var now = clock.getTime();

		while (true) {
			var timer = timers.popOldestTimer(now);
			if (timer == null)
				break;

			log('process timer ${timer}');
			try {
				var object = objectLoader.loadObject(timer.oid, SThing);
				var cb = Reflect.field(object, timer.method);
				cb(0);
			} catch (f: Fault) {
				log('timer failed: ${f.detail} ${f.getStackTrace()}');
			} catch (e: Exception) {
				var f = Fault.wrap(e);
				log('timer failed: ${f.detail} ${f.getStackTrace()}');
			}
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

