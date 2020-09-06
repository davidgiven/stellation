package commands;

import interfaces.IConsole;
import interfaces.IDatastore;
import utils.GetOpt.getopt;
import utils.Injectomatic.inject;
import utils.Flags;
import utils.Fault;
import utils.FaultDomain.SYNTAX;
import tink.CoreApi;
import model.ObjectLoader;
import model.SUniverse;
import model.SGalaxy;
import model.SPlayer;
using utils.NullTools;

@:tink
class AbstractCommand<Req, Res> {
    @:lazy var console = inject(IConsole);
	@:lazy var objectLoader = inject(ObjectLoader);
	@:lazy var datastore = inject(IDatastore);
	@:lazy var universe = inject(SUniverse);
	@:lazy var galaxy = inject(SGalaxy);
	@:lazy var player = inject(SPlayer);

	public function callAsync(argv: Array<String>): Promise<Noise> {
		throw Fault.UNIMPLEMENTED;
	}

	public function callSync(argv: Array<String>): Void {
		render(callRemote(argv));
	}
		
	public function callRemote(argv: Array<String>): Res {
		var req = parse(argv);
		return run(argv, req);
	}

	public function parse(argv: Array<String>): Req {
        throw Fault.UNIMPLEMENTED;
	}

	public function run(argv: Array<String>, req: Req): Res {
        throw Fault.UNIMPLEMENTED;
	}

    public function render(res: Res) {
        throw Fault.UNIMPLEMENTED;
    }
}

