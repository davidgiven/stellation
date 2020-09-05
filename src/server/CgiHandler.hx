package server;

import utils.Fault;
import utils.FaultDomain;
import haxe.Serializer;
import haxe.Unserializer;
import tink.CoreApi;
import interfaces.ILogger.Logger.log;
import utils.Injectomatic.bind;
import model.SUniverse;
import model.SGalaxy;

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
				var universe = findUniverse();
				bind(SUniverse, universe);
				bind(SGalaxy, universe.galaxy);

				var session: Int = u.unserialize();
				var username: String = u.unserialize();
				var password: String = u.unserialize();
				log('start request from $username');
				var player = authenticator.authenticatePlayer(username, password);
				log('authenticated as ${player.oid}');

				var argv: Array<String> = u.unserialize();
				var res: Dynamic = null;
				var fault: Fault = null;
				datastore.withTransaction(() -> {
					log('running: $argv');
					try {
						res = commandDispatcher.remoteCall(argv);
						log('response: $res');
					} catch (f: Fault) {
						log('fault: $f');
						fault = f;
					}
				});
				
				Sys.println("Content-type: text/plain; charset=utf-8");
				Sys.println("");

				var s = new Serializer();
				s.useCache = true;
				s.serialize(session);
				if (fault != null) {
					s.serialize({
						status: fault.status,
						domain: fault.domain,
						detail: fault.detail
					});
				} else {
					s.serialize(null);
				}
				s.serialize(res);
				Sys.println(s.toString());
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

