package model;

import utils.Injectomatic;
import utils.Injectomatic.bind;
import utils.Oid;
import runtime.cpp.Sqlite;
import runtime.cpp.SqlDatastore;
import interfaces.IDatastore;
import interfaces.IClock;
import interfaces.ILogger;
import runtime.shared.ServerClock;
import runtime.shared.Time;
import runtime.shared.TraceLogger;
import model.SUniverse;
import model.SGalaxy;
import model.SStar;
import model.SPlayer;
import model.SShip;
import model.SJumpdrive;
import model.ObjectLoader;
import model.Syncer;
import utest.Assert;
import tink.CoreApi;
import interfaces.RPC;
using model.ThingTools;
using utils.ArrayTools;

typedef PropTuple = {
	oid: Oid,
	name: String
};

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
		bind(ILogger, new TraceLogger());

        bind(SqliteDatabase, Sqlite.open(":memory:"));
		datastore = new SqlDatastore();
        datastore.initialiseDatabase();
		bind(IDatastore, datastore);
		
		objectLoader = new ObjectLoader();
		bind(ObjectLoader, objectLoader);
        objectLoader.initialiseProperties();
		
		universe = objectLoader.createUniverse();
		bind(SUniverse, universe);

		var god = objectLoader.createGod();

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

	public function testVisibleObjects() {
		Assert.same(
			[star1, player1, ship1, jumpdrive1, star2, ship2, jumpdrive2, shipx, jumpdrivex].toMap(),
			player1.calculateVisibleObjects());
		Assert.same(
			[star2, playerx, shipx, jumpdrivex, ship2, jumpdrive2].toMap(),
			playerx.calculateVisibleObjects());
	}

	public function testInitialSync() {
		var session = datastore.createSyncSession();

		var p = syncer.exportSyncPacket(player1, session);
		Assert.same([
			universe.oid,
			universe.galaxy.oid,
			star1.oid,
			star2.oid,
			player1.oid,
			ship1.oid,
			jumpdrive1.oid,
			ship2.oid,
			jumpdrive2.oid,
			shipx.oid,
			jumpdrivex.oid
		].toMap(), [for (k in p.keys()) k].toMap());
	}

	public function testIncrementalSync() {
        var session = datastore.createSyncSession();

        /* Initial sync */

        syncer.exportSyncPacket(player1, session);

        /* First incremental sync, with no changes. */

		var p = syncer.exportSyncPacket(player1, session);
		Assert.same(([]: Array<PropTuple>), getChangedProperties(p));

		/* Change one property and try again. */

        star1.name = "Fnord";
		p = syncer.exportSyncPacket(player1, session);
		Assert.same([
				{ oid: star1.oid, name: "name" },
			].toMap(), getChangedProperties(p).toMap());
		Assert.same("Fnord", p[star1.oid]["name"]);

        /* Sync again with no changes. */

        p = syncer.exportSyncPacket(player1, session);
        Assert.same(([]: Array<PropTuple>), getChangedProperties(p));

        /* ship2 moves to star1, rendering star2 invisible. */

        ship2.moveTo(star1);
        p = syncer.exportSyncPacket(player1, session);
		Assert.isFalse(p.exists(shipx.oid)); /* can't see shipx any more */

        /* An invisible object changes state. */

        shipx.name = "Floop";
        p = syncer.exportSyncPacket(player1, session);
        Assert.same(([]: Array<PropTuple>), getChangedProperties(p));

        /* ship2 moves back to star2. Now player1 can see shipx's new name. */

        ship2.moveTo(star2);
        p = syncer.exportSyncPacket(player1, session);
		Assert.same("Floop", p[shipx.oid]["name"]);
	}

	private static function getChangedProperties(syncMessage: SyncMessage): Array<PropTuple> {
		var r: Array<PropTuple> = [];
		for (oid => ps in syncMessage) {
			for (name => value in ps) {
				r.push({ oid: oid, name: name });
			}
		}
		return r;
	}
}

