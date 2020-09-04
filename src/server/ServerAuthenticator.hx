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
import interfaces.ILogger.Logger.log;

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
                password TEXT,
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

    public function authenticatePlayer(username: String, password: String): SPlayer {
        var result = database.sqlStatement('SELECT oid, password FROM players WHERE username=?')
				.bindString(1, username)
                .executeSimpleQuery();
        if (result == null) {
            throw Fault.AUTH_FAILED;
        }
        var oid = result["oid"].toOid();
        var hash = result["password"].toString();

        if (!BCrypt.verify(password, hash)) {
            throw Fault.AUTH_FAILED;
        }
        authenticatedPlayer = objectLoader.loadObject(oid, SPlayer);
        return authenticatedPlayer;
    }

    public function setPassword(player: SPlayer, password: String): Void {
        var salt = BCrypt.generateSalt(10, BCrypt.Revision2B);
        var hashed = BCrypt.encode(password, salt);

        database.sqlStatement('UPDATE players SET password=? WHERE username=?')
                .bindString(1, hashed)
                .bindString(2, player.username)
                .executeStatement();
    }

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

