package server;

import utils.Fault;
import utils.FaultDomain;
import haxe.Unserializer;
import tink.CoreApi;
import interfaces.ILogger.Logger.log;

@:tink
class CgiHandler extends AbstractHandler {
	public function new() {
		super();
	}

    public function main(): Void {
		try {
			if (Sys.getEnv("REQUEST_METHOD") != "POST") {
				throw badCgiException("request is not POST");
			}

			var contentLength = Std.parseInt(Sys.getEnv("CONTENT_LENGTH"));
			if (contentLength == null) {
				throw badCgiException("bad or missing content length");
			}

			var body = Sys.stdin().read(contentLength).toString();

			var u = new Unserializer(body);
			u.setResolver(null);

			withServer(Configuration.DATABASE_PATH, () -> {
				var session = u.unserialize();
				var username = u.unserialize();
				var password = u.unserialize();
				log('start request from $username');
				authenticator.authenticatePlayer(username, password);

				Sys.println("Content-type: text/plain; charset=utf-8");
				Sys.println("");
				Sys.println("fnord");
			});
		} catch (f: Fault) {
			log('fail: $f');
            Sys.println("Content-type: text/plain; charset=utf-8");
            Sys.println('Status: ${f.status}');
            Sys.println("");
			Sys.println(f.detail);
			trace(f);
		} catch (d: Dynamic) {
			log('fail: $d');
            Sys.println("Content-type: text/plain; charset=utf-8");
            Sys.println('Status: 500');
            Sys.println("");
			trace(d);
		}
	}

	private static function badCgiException(s: String): Fault {
		return new Fault(NETWORK).withDetail('Bad CGI request: $s');
	}
}

