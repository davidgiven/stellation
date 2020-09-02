package server;

import interfaces.IDatastore;
import runtime.shared.InMemoryDatastore;
import model.ObjectLoader;
import utils.Injectomatic.bind;
import utils.Oid;

class CliHandler {
    public function new() {}

    private function help(arg: String): Void {
        Sys.println("Stellation7 server");
        Sys.println("Syntax: stellation [<option>] <command> [<command args>]");
        Sys.println("  --help            Shows this message");
        Sys.println("  --db=<filename>   Use this filename for the database file");
        Sys.exit(0);
    }

    public function main(argv: Array<String>) {
        help("");
    }
}

