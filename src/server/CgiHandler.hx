package server;

import utils.Fault;
import utils.FaultDomain;
import haxe.Serializer;
import haxe.Unserializer;
import tink.CoreApi;
import interfaces.ILogger.Logger.log;
import interfaces.RPC;
import utils.Injectomatic.bind;
import model.SUniverse;
import model.SGalaxy;
import model.Syncer;

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
			var rpcReq: RpcRequest = u.unserialize();

			withServer(Configuration.DATABASE_PATH, () -> {
				var universe = findUniverse();
				bind(SUniverse, universe);
				bind(SGalaxy, universe.galaxy);

				if (rpcReq.syncSession == 0) {
					rpcReq.syncSession = datastore.createSyncSession();
				}
				log('start request from $rpcReq.username, session $rpcReq.session');
				var player = authenticator.authenticatePlayer(rpcReq.username, rpcReq.password);
				log('authenticated as ${player.oid}');

				var res: Dynamic = null;
				var fault: Fault = null;
				datastore.withTransaction(() -> {
					log('running: $rpcReq');
					try {
						res = commandDispatcher.remoteCall(rpcReq.argv);
						log('response: $res');
					} catch (f: Fault) {
						log('fault: $f');
						fault = f;
					}
				});
				
				var rpcRes: RpcResponse = {
					syncSession: rpcReq.syncSession,
					player: player.oid,
					fault: if (fault != null) fault.serialise() else null,
					syncData: new Syncer().exportSyncPacket(player, rpcReq.syncSession),
					response: res
				};

				var s = new Serializer();
				s.useCache = true;
				s.serialize(rpcRes);

				Sys.println("Content-type: text/plain; charset=utf-8");
				Sys.println("");
				Sys.println(s.toString());
			});
		} catch (f: Fault) {
			log('fail: $f');
            Sys.println("Content-type: text/plain; charset=utf-8");
            Sys.println('Status: ${f.status}');
            Sys.println("");
			Sys.println(f.detail);
			f.dumpStackTrace(Sys.stdout());
		} catch (d: Dynamic) {
			log('fail: $d');
            Sys.println("Content-type: text/plain; charset=utf-8");
            Sys.println('Status: 500');
            Sys.println("");
			throw d;
		}
	}

	private static function badCgiException(s: String): Fault {
		return new Fault(NETWORK).withDetail('Bad CGI request: $s');
	}
}

