package server;

import interfaces.IAuthenticator;
import tink.CoreApi;
import utils.Fault;
import utils.Oid;
import utils.Injectomatic.inject;
import runtime.cpp.Sqlite;
import model.ObjectLoader;
import model.SPlayer;
import haxe.crypto.BCrypt;

@:tink
@async
class ServerAuthenticator implements IAuthenticator {
    @:lazy private var objectLoader = inject(ObjectLoader);
    @:lazy private var database = inject(SqliteDatabase);

    private var authenticatedPlayer: SPlayer = null;

    public function new() {}

    public function initialiseDatabase(): Void {
        database.executeSql('
            CREATE TABLE IF NOT EXISTS players (
                username TEXT PRIMARY KEY,
                oid NOT NULL REFERENCES objects(oid) ON DELETE CASCADE
            )
        ');
    }

    public function setAuthenticatedPlayer(player: SPlayer): Void {
        authenticatedPlayer = player;
    }

    public function getAuthenticatedPlayer(): SPlayer {
        return authenticatedPlayer;
    }

    @async public function authenticatePlayer(username: String, password: String): Noise throw Fault.UNIMPLEMENTED;

    public function setPassword(player: SPlayer, password: String): Void throw Fault.UNIMPLEMENTED;

    public function registerPlayer(player: SPlayer): Void {
        database.sqlStatement('INSERT OR REPLACE INTO players (username, oid) VALUES (?, ?)')
                .bindString(1, player.username)
                .bindOid(2, player.oid)
                .executeStatement();
    }

    public function getPlayers(): Iterable<SPlayer> {
        var results = new Array<SPlayer>();
        for (result in database.sqlStatement('SELECT oid FROM players').executeQuery()) {
            var oid = result.get("oid").toOid();
            results.push(objectLoader.loadObject(oid, SPlayer));
        }
        return results;
    }
}

