package model;

import utils.Injectomatic;
import utils.Injectomatic.bind;
import runtime.cpp.Sqlite;
import runtime.cpp.SqlDatastore;
import interfaces.IDatastore;
import interfaces.IClock;
import runtime.shared.ServerClock;
import model.SUniverse;
import model.SGalaxy;
import model.ObjectLoader;
import utest.Assert;

class SyncerTest extends TestCase {
	var datastore: SqlDatastore;
	var objectLoader: ObjectLoader;
	var universe: SUniverse;
	var syncer: Syncer;

	function setup() {
		Injectomatic.resetBindingsForTest();
		bind(IClock, new ServerClock());

        bind(SqliteDatabase, Sqlite.open(":memory:"));
		datastore = new SqlDatastore();
        datastore.initialiseDatabase();
		bind(IDatastore, datastore);
		
		objectLoader = new ObjectLoader();
		bind(ObjectLoader, objectLoader);
        objectLoader.initialiseProperties();
		
		universe = objectLoader.createUniverse();
		bind(SUniverse, universe);

		universe.galaxy = objectLoader.createObject(SGalaxy);
		bind(SGalaxy, universe.galaxy);

		syncer = new Syncer();
	}

	public function test() {
		Assert.isTrue(true);
	}
}

