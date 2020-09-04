package server;

import utils.Fault;
import utils.FaultDomain;
import haxe.Unserializer;
import tink.CoreApi;

@:tink
@await
class CgiHandler extends AbstractHandler {
	private static final DATABASE = "/home/dg/nonshared/stellation/stellation.sqlite";

	public function new() {
		super();
	}

    @async
    public function main(): Noise {
		if (Sys.getEnv("REQUEST_METHOD") != "POST") {
			throw badCgiException("request is not POST");
		}

		var path = Sys.getEnv("PATH_INFO") | if (null) "";

		var contentLength = Std.parseInt(Sys.getEnv("CONTENT_LENGTH"));
		if (contentLength == null) {
			throw badCgiException("bad or missing content length");
		}

		var body = Sys.stdin().read(contentLength).toString();

		var u = new Unserializer(body);
		u.setResolver(null);

        withServer(DATABASE, () -> {
			var username = u.unserialize();
			var password = u.unserialize();
			@await authenticator.authenticatePlayer(username, password);
		});

		return Noise;
	}

	private static function badCgiException(s: String): Fault {
		return new Fault(NETWORK).withDetail('Bad CGI request: $s');
	}
}

