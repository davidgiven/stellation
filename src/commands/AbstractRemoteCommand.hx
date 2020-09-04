package commands;

import utils.Fault;
import haxe.Serializer;

@await
class AbstractRemoteCommand<Req, Res> extends AbstractCommand<Req, Res> {
	public function remoteCall(argv: Array<String>): Res {
		var req = parse(argv);
		var res = serverRun(argv, req);
		return res;
	}

    @async override function run(argv: Array<String>, req: Req): Res {
        var s = new Serializer();
        s.useCache = true;
        s.serialize(argv);
        trace(s.toString());
    }

    public function serverRun(argv: Array<String>, req: Req): Res {
        throw Fault.UNIMPLEMENTED;
    }
}


