package model;

import utils.Injectomatic;
import utils.Injectomatic.bind;
import interfaces.IClock;
import interfaces.IDatastore;
import runtime.shared.ServerClock;
import runtime.cpp.SqlDatastore;
import model.ObjectLoader;

class SqlObjectsTest extends AbstractObjectsTest {
	override function setup() {
		Injectomatic.resetBindingsForTest();
		bind(IClock, new ServerClock());

		var datastore = new SqlDatastore(":memory:");
        datastore.initialiseDatabase();
		bind(IDatastore, datastore);

        bind(ObjectLoader, new ObjectLoader());

		super.setup();
	}
}
