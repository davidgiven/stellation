package server;

import interfaces.IDatastore;
import model.ObjectLoader;
import model.SGalaxy;
import model.SUniverse;
import runtime.cpp.SqlDatastore;
import commands.CommandDispatcher;
import utils.Flags;
import utils.GetOpt.getopt;
import utils.Injectomatic.bind;
import utils.Oid;
import utils.Fault;

@async
class CliHandler extends AbstractHandler {
    private static var databaseFile: String = null;

    private static final flags = new Flags()
        .addString("--db", f -> databaseFile = f)
    ;

    public function new() {
        super();
    }

    private function help(arg: String): Void {
        Sys.println("Stellation7 server");
        Sys.println("Syntax: stellation [<option>] <command> [<command args>]");
        Sys.println("  --help            Shows this message");
        Sys.println("  --db=<filename>   Use this filename for the database file");
        Sys.exit(0);
    }

    @await
    public function main(argv: Array<String>) {
        var remaining = getopt(argv, flags);

        if (databaseFile == null) {
            fatalError("you must supply a database filename");
        }

        var commandDispatcher = new CommandDispatcher();
        bind(CommandDispatcher, commandDispatcher);

        withServer(databaseFile, () -> {
            var universe = datastore.withTransaction(() -> findOrCreateUniverse());

            var commandShell = 
            datastore.withTransaction(() -> {
                @await commandDispatcher.callArgv(remaining);
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

            return universe;
        }
    }

    public static function fatalError(message: String) {
        Sys.println(message);
        Sys.exit(1);
    }
}

