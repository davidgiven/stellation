package runtime.shared;

import utils.Injectomatic;
import utils.Injectomatic.bind;
import interfaces.IClock;
import interfaces.IDatastore;
import runtime.shared.ServerClock;
import runtime.shared.InMemoryDatastore;
import runtime.shared.Time;

class InMemoryDatastoreTest extends AbstractDatastoreTest {
	override function setup() {
		Injectomatic.resetBindingsForTest();
		bind(IClock, new ServerClock());
		bind(Time, new Time());

		var datastore = new InMemoryDatastore();
		bind(IDatastore, datastore);
        datastore.initialiseDatabase();

		super.setup();
	}
}

