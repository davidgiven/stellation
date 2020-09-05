package model;

import utils.Injectomatic;
import utils.Injectomatic.bind;
import interfaces.IClock;
import interfaces.IDatastore;
import runtime.shared.ServerClock;
import runtime.shared.InMemoryDatastore;
import runtime.shared.Time;
import model.ObjectLoader;

class InMemoryObjectsTest extends AbstractObjectsTest {
	override function setup() {
		Injectomatic.resetBindingsForTest();
		bind(IClock, new ServerClock());
		bind(Time, new Time());

		var datastore = new InMemoryDatastore();
		bind(IDatastore, datastore);
        datastore.initialiseDatabase();

        bind(ObjectLoader, new ObjectLoader());

		super.setup();
	}
}

