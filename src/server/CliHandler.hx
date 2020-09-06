package server;

import interfaces.IDatastore;
import model.ObjectLoader;
import model.SGalaxy;
import model.SUniverse;
import model.SPlayer;
import model.SShip;
import model.SJumpdrive;
import model.SStar;
import runtime.cpp.SqlDatastore;
import commands.CommandDispatcher;
import utils.Flags;
import utils.GetOpt.getopt;
import utils.Injectomatic.bind;
import utils.Oid;
import utils.Fault;
import tink.CoreApi;
import haxe.Exception;
using model.ThingTools;

class CliHandler extends AbstractHandler {
    private static var databaseFile: String = null;
    private static var userOid: Null<Oid> = null;

    private static final flags = new Flags()
        .addString("--db", f -> databaseFile = f)
        .addInt("--user", i -> userOid = i)
    ;

    public function new() {
        super();
    }

    private function help(arg: String): Void {
        Sys.println("Stellation7 server");
        Sys.println("Syntax: stellation [<option>] <command> [<command args>]");
        Sys.println("  --help            Shows this message");
        Sys.println("  --db=<filename>   Use this filename for the database file");
        Sys.println("  --user=<oid>      OID of user to act as (otherwise, nobody)");
        Sys.exit(0);
    }

    public function main(argv: Array<String>): Void {
        var remaining = getopt(argv, flags);

        if (databaseFile == null) {
            fatalError("you must supply a database filename");
        }

        withServer(databaseFile, () -> {
            var universe = datastore.withTransaction(() -> findOrCreateUniverse());
			bind(SUniverse, universe);
			bind(SGalaxy, universe.galaxy);

            var player = objectLoader.loadObject(userOid, SPlayer);
            authenticator.setAuthenticatedPlayer(player);
			bind(SPlayer, player);

            datastore.withTransaction(() -> {
				try {
					commandDispatcher.serverCall(remaining);
				} catch (f: Fault) {
					Sys.println(f.detail);
					f.dumpStackTrace(Sys.stdout());
					f.rethrow();
				} catch (e: Exception) {
					var f = Fault.wrap(e);
					Sys.println(f.detail);
					f.dumpStackTrace(Sys.stdout());
					f.rethrow();
				}
            });
        });
    }

    public function findOrCreateUniverse(): SUniverse {
        try {
            return findUniverse();
        } catch (f: Fault) {
            var universe = objectLoader.createUniverse();
            var god = objectLoader.createGod();

            universe.galaxy = objectLoader.createObject(SGalaxy);
            universe.galaxy.initialiseGalaxy();

            god.name = "God";
            god.username = "god";
            authenticator.registerPlayer(god);
            authenticator.setPassword(god, "fnord");

			var player = objectLoader.createObject(SPlayer);
			player.name = "Player";
			player.username = "player";
			player.owner = player;
			authenticator.registerPlayer(player);
			authenticator.setPassword(player, "fnord");

			var ship = objectLoader.createObject(SShip);
			ship.owner = player;
			ship.moveTo(objectLoader.loadObject(4, SStar));
			player.ships.add(ship);

			var jumpdrive = objectLoader.createObject(SJumpdrive);
			jumpdrive.owner = player;
			jumpdrive.moveTo(ship);

            return universe;
        }
    }

    public static function fatalError(message: String) {
        Sys.println(message);
        Sys.exit(1);
    }
}

