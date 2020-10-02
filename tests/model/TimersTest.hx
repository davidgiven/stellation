package model;

import utils.Injectomatic;
import utils.Injectomatic.bind;
import utils.Injectomatic.inject;
import interfaces.IClock;
import interfaces.IDatastore;
import interfaces.ITimers;
import runtime.shared.ServerClock;
import runtime.shared.Time;
import runtime.cpp.Sqlite;
import runtime.cpp.SqlDatastore;
import utest.Assert;
import model.ObjectLoader;

@:tink
class TimersTest extends TestCase {
	@:calc var database = inject(SqliteDatabase);
	@:calc var datastore = inject(IDatastore);
	@:calc var objectLoader = inject(ObjectLoader);
	@:calc var timers = inject(ITimers);

	var star1: SStar;
	var star2: SStar;
	var star3: SStar;
	var star4: SStar;

	function setup() {
		Injectomatic.resetBindingsForTest();
		bind(IClock, new ServerClock());
		bind(Time, new Time());

        bind(SqliteDatabase, Sqlite.open(":memory:"));
		var datastore = new SqlDatastore();
        datastore.initialiseDatabase();
		bind(IDatastore, datastore);
		bind(ITimers, datastore);

		database.executeSql("BEGIN");

        bind(ObjectLoader, new ObjectLoader());
		objectLoader.initialiseProperties();

        star1 = objectLoader.createObject(SStar);
        star2 = objectLoader.createObject(SStar);
        star3 = objectLoader.createObject(SStar);
        star4 = objectLoader.createObject(SStar);
	}

	function teardown() {
		database.executeSql("COMMIT");
		database.close();
	}

	function testTimerOrder() {
        timers.setTimer(star1.oid, "method", 1.0);
        timers.setTimer(star2.oid, "method", 10.0);
        timers.setTimer(star3.oid, "method", 8.0);
        timers.setTimer(star4.oid, "method", 99.0);
		
		Assert.same(4, countTimers());
		Assert.same(
			[star1.oid, star3.oid, star2.oid, star4.oid],
			popAllTimers(9999.0).map(t -> t.oid));
		Assert.same(0, countTimers());
	}

	private function countTimers(): Int {
		return database.sqlStatement("SELECT COUNT(*) AS count FROM timers")
			.executeSimpleQuery()
			.get("count")
			.toInt();
	}

	private function popAllTimers(maxExpiry: Float): Array<Timer> {
		var a: Array<Timer> = [];
		while (true) {
			var t = timers.popOldestTimer(maxExpiry);
			if (t == null)
				break;
			a.push(t);
		}
		return a;
	}
}

