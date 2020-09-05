package model;

import utils.Injectomatic;
import utils.Injectomatic.bind;
import runtime.cpp.Sqlite;
import runtime.cpp.SqlDatastore;
import interfaces.IDatastore;
import interfaces.IClock;
import runtime.shared.ServerClock;
import runtime.shared.Time;
import model.SUniverse;
import model.SGalaxy;
import model.SStar;
import model.SPlayer;
import model.SShip;
import model.SJumpdrive;
import model.ObjectLoader;
import utest.Assert;
using model.ThingTools;

class SyncerTest extends TestCase {
	var datastore: SqlDatastore;
	var objectLoader: ObjectLoader;
	var universe: SUniverse;
	var syncer: Syncer;
	var star1: SStar;
	var star2: SStar;

	var player1: SPlayer;
	var ship1: SShip;
	var ship2: SShip;
	var jumpdrive1: SJumpdrive;
	var jumpdrive2: SJumpdrive;

	var playerx: SPlayer;
	var shipx: SShip;
	var jumpdrivex: SJumpdrive;

	function setup() {
		Injectomatic.resetBindingsForTest();
		bind(IClock, new ServerClock());
		bind(Time, new Time());

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

        star1 = objectLoader.createObject(SStar).moveTo(universe.galaxy);
        star2 = objectLoader.createObject(SStar).moveTo(universe.galaxy);

		/* Player 1 has two ships, one each in star1 and star2 */

        player1 = objectLoader.createObject(SPlayer);
        player1.owner = player1;

        ship1 = objectLoader.createObject(SShip);
        ship1.owner = player1;
        player1.ships.add(ship1);
        ship1.moveTo(star1);

        jumpdrive1 = objectLoader.createObject(SJumpdrive);
        jumpdrive1.owner = player1;
        jumpdrive1.moveTo(ship1);

        ship2 = objectLoader.createObject(SShip);
        ship2.owner = player1;
        player1.ships.add(ship2);
        ship2.moveTo(star2);

        jumpdrive2 = objectLoader.createObject(SJumpdrive);
        jumpdrive2.owner = player1;
        jumpdrive2.moveTo(ship2);

		/* Player x has one ship, in star2 */

        playerx = objectLoader.createObject(SPlayer);
        playerx.owner = player1;

        shipx = objectLoader.createObject(SShip);
        shipx.owner = playerx;
        playerx.ships.add(shipx);
        shipx.moveTo(star2);

        jumpdrivex = objectLoader.createObject(SJumpdrive);
        jumpdrivex.owner = playerx;
        jumpdrivex.moveTo(shipx);

		syncer = new Syncer();
	}

    public function testSyncSession() {
        Assert.same(1, datastore.createSyncSession());
        Assert.same(2, datastore.createSyncSession());
    }
}

