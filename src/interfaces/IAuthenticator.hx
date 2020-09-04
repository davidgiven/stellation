package interfaces;

import tink.CoreApi;
import utils.Oid;
import model.SPlayer;

@async
interface IAuthenticator {
    public function initialiseDatabase(): Void;

    public function setAuthenticatedPlayer(player: SPlayer): Void;
    public function authenticatePlayer(username: String, password: String): SPlayer;
    public function getAuthenticatedPlayer(): SPlayer;

    public function setPassword(player: SPlayer, password: String): Void;
    public function registerPlayer(player: SPlayer): Void;

    public function getPlayers(): Iterable<SPlayer>;
}

