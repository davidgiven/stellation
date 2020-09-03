package commands;

import utils.Fault;
import haxe.Serializer;
import haxe.Unserializer;

@await
class AbstractRemoteCommand<Req, Res> extends AbstractCommand<Req, Res> {
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


