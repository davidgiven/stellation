package server;

import interfaces.IClock;
import interfaces.IDatastore;
import interfaces.ITime;
import model.ObjectLoader;
import model.SUniverse;
import runtime.cpp.SqlDatastore;
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

    public function findUniverse(): SUniverse {
        return objectLoader.loadObject(1, SUniverse);
    }

    public function withServer(filename: String, callback: () -> Void): Void {
		bind(IClock, new ServerClock());
		bind(ITime, new Time());
		bind(Random, new Random());

        var datastore = new SqlDatastore(filename);
        bind(IDatastore, datastore);
        bind(ObjectLoader, new ObjectLoader());

        datastore.withTransaction(() -> {
            datastore.initialiseDatabase();
            objectLoader.initialiseProperties();
        });

        try {
            callback();
            datastore.close();
        } catch (f) {
			trace(f);
            datastore.close();
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

