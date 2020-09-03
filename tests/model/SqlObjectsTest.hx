package model;

import utils.Injectomatic;
import utils.Injectomatic.bind;
import interfaces.IClock;
import interfaces.IDatastore;
import runtime.shared.ServerClock;
import runtime.cpp.Sqlite;
import runtime.cpp.SqlDatastore;
import model.ObjectLoader;

class SqlObjectsTest extends AbstractObjectsTest {
	override function setup() {
		Injectomatic.resetBindingsForTest();
		bind(IClock, new ServerClock());

        bind(SqliteDatabase, Sqlite.open(":memory:"));
		var datastore = new SqlDatastore();
        datastore.initialiseDatabase();
		bind(IDatastore, datastore);

        bind(ObjectLoader, new ObjectLoader());

		super.setup();
	}
}
