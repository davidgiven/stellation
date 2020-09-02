package runtime.shared;

import utils.Injectomatic;
import utils.Injectomatic.bind;
import interfaces.IClock;
import interfaces.IDatastore;
import runtime.shared.ServerClock;
import runtime.shared.InMemoryDatastore;
import runtime.cpp.Database;
import sys.db.Sqlite;

class SqlDatastoreTest extends AbstractDatastoreTest {
	override function setup() {
		Injectomatic.resetBindingsForTest();
		bind(IClock, new ServerClock());

        var database = new Database();
        bind(Database, database);
//		var datastore = new SqlDatastore();
//		bind(IDatastore, datastore);

        database.openDatabase(":memory:");
//        datastore.initialiseDatabase();

		super.setup();
	}
}


