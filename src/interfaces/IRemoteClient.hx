package interfaces;

import tink.CoreApi;

interface IRemoteClient {
	public function setCredentials(username: String, password: String): Void;
	public function call<Req>(argv: Array<String>): Promise<Req>;
}

