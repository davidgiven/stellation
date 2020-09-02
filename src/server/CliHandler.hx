package server;

import interfaces.IDatastore;
import model.ObjectLoader;
import runtime.cpp.SqlDatastore;
import utils.Flags;
import utils.Injectomatic.bind;
import utils.Oid;
import utils.GetOpt.getopt;

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

    public function main(argv: Array<String>) {
        var remaining = getopt(argv, flags);

        if (databaseFile == null) {
            fatalError("you must supply a database filename");
        }

        withServer(databaseFile, () -> {
            var universe = findOrCreateUniverse();
        });
    }

    public static function fatalError(message: String) {
        Sys.println(message);
        Sys.exit(1);
    }
}

