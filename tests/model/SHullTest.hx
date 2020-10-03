package model;

import utils.Injectomatic;
import utils.Injectomatic.bind;
import utils.Oid;
import utils.Fault;
import utils.FaultDomain.INVALID_ARGUMENT;
import runtime.cpp.Sqlite;
import runtime.cpp.SqlDatastore;
import runtime.shared.ServerClock;
import runtime.shared.Time;
import runtime.shared.TraceLogger;
import interfaces.IDatastore;
import interfaces.IClock;
import interfaces.ILogger;
import model.Properties;
import model.SHull;
import model.SShip;
import model.ObjectLoader;
import haxe.Exception;
import utest.Assert;

class SHullTest extends TestCase {
	var datastore: SqlDatastore;
	var objectLoader: ObjectLoader;
	var hull: SHull;

	function setup() {
		Injectomatic.resetBindingsForTest();
		bind(IClock, new ServerClock());
		bind(Time, new Time());
		bind(ILogger, new TraceLogger());

        bind(SqliteDatabase, Sqlite.open(":memory:"));
		datastore = new SqlDatastore();
        datastore.initialiseDatabase();
		bind(IDatastore, datastore);
		
		objectLoader = new ObjectLoader();
		bind(ObjectLoader, objectLoader);
        objectLoader.initialiseProperties();

		hull = objectLoader.createObject(SHull);
	}

	function testValidationWithBadData() {
		var f = captureFault(() -> { hull.setFrameData(null); });
		Assert.same(f.domain, INVALID_ARGUMENT);
	}

	function testValidationWithTooSmall() {
		var f = captureFault(() -> { hull.setFrameData({ width: -1, height: -1, modules: [] }); });
		Assert.same(f.domain, INVALID_ARGUMENT);
	}

	function testValidationWithTooLarge() {
		var f = captureFault(() -> { hull.setFrameData({ width: 1000, height: 1000, modules: [] }); });
		Assert.same(f.domain, INVALID_ARGUMENT);
	}

	function testValidationWithMismatchedModuleData() {
		var f = captureFault(() -> { hull.setFrameData({ width: 1, height: 1, modules: [ null, null ] }); });
		Assert.same(f.domain, INVALID_ARGUMENT);
	}

	function testValidationWithBadModuleType() {
		var f = captureFault(() -> { hull.setFrameData({ width: 1, height: 1, modules: [ "flob" ] }); });
		Assert.same(f.domain, INVALID_ARGUMENT);
	}

	function testValidationWithNotAModule() {
		var f = captureFault(() -> { hull.setFrameData({ width: 1, height: 1, modules: [ "SShip" ] }); });
		Assert.same(f.domain, INVALID_ARGUMENT);
	}
}

