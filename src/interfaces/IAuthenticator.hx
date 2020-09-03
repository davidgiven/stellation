package interfaces;

import tink.CoreApi;
import utils.Oid;

@async
interface IAuthenticator {
    public function initialiseDatabase(): Void;

    public function setAuthenticatedPlayer(oid: Null<Oid>): Void;
    @async public function authenticatePlayer(username: String, password: String): Noise;
    public function getAuthenticatedPlayer(): Null<Oid>;

    public function setPassword(playerOid: Oid, password: String): Void;
    public function registerPlayer(username: String, playerOid: Oid): Void;
}

