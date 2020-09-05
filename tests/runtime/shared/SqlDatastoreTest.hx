package runtime.shared;

import utils.Injectomatic;
import utils.Injectomatic.bind;
import interfaces.IClock;
import interfaces.IDatastore;
import runtime.shared.ServerClock;
import runtime.shared.Time;
import runtime.cpp.Sqlite;
import runtime.cpp.SqlDatastore;

class SqlDatastoreTest extends AbstractDatastoreTest {
	override function setup() {
		Injectomatic.resetBindingsForTest();
		bind(IClock, new ServerClock());
		bind(Time, new Time());

        bind(SqliteDatabase, Sqlite.open(":memory:"));
		var datastore = new SqlDatastore();
        datastore.initialiseDatabase();
		bind(IDatastore, datastore);

		super.setup();
	}
}


