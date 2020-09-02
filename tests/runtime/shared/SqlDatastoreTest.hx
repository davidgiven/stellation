package runtime.shared;

import utils.Injectomatic;
import utils.Injectomatic.bind;
import interfaces.IClock;
import interfaces.IDatastore;
import runtime.shared.ServerClock;
import runtime.cpp.SqlDatastore;
import sys.db.Sqlite;

class SqlDatastoreTest extends AbstractDatastoreTest {
	override function setup() {
		Injectomatic.resetBindingsForTest();
		bind(IClock, new ServerClock());

		var datastore = new SqlDatastore(":memory:");
        datastore.initialiseDatabase();
		bind(IDatastore, datastore);

		super.setup();
	}
}


