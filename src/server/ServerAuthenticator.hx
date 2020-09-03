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

    private var authenticatedPlayer: Null<Oid> = null;

    public function new() {}

    public function initialiseDatabase(): Void {
        database.executeSql('
            CREATE TABLE IF NOT EXISTS players (
                username TEXT PRIMARY KEY,
                oid NOT NULL REFERENCES objects(oid) ON DELETE CASCADE
            )
        ');
    }

    public function setAuthenticatedPlayer(oid: Null<Oid>): Void {
        var player = objectLoader.loadObject(oid, SPlayer);
        authenticatedPlayer = oid;
    }

    public function getAuthenticatedPlayer(): Null<Oid> {
        return authenticatedPlayer;
    }

    @async public function authenticatePlayer(username: String, password: String): Noise throw Fault.UNIMPLEMENTED;
    public function setPassword(playerOid: Oid, password: String): Void throw Fault.UNIMPLEMENTED;

    public function registerPlayer(username: String, playerOid: Oid): Void {
        database.sqlStatement('INSERT OR REPLACE INTO players (username, oid) VALUES (?, ?)')
                .bindString(1, username)
                .bindOid(2, playerOid)
                .executeStatement();
    }
}

