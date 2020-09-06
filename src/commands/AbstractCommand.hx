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
class AbstractCommand<Res> {
    @:lazy var console = inject(IConsole);
	@:lazy var objectLoader = inject(ObjectLoader);
	@:lazy var datastore = inject(IDatastore);
	@:lazy var universe = inject(SUniverse);
	@:lazy var galaxy = inject(SGalaxy);
	@:lazy var player = inject(SPlayer);

	var argv: Array<String> = [];

	public function setArgv(argv: Array<String>): AbstractCommand<Res> {
		this.argv = argv;
		return this;
	}
	
	public function callAsync(): Promise<Noise> {
		throw Fault.UNIMPLEMENTED;
	}

	public function callSync(): Void {
		render(callRemote());
	}
		
	public function callRemote(): Res {
		parse();
		return run();
	}

	public function parse(): Void {
        throw Fault.UNIMPLEMENTED;
	}

	public function run(): Res {
        throw Fault.UNIMPLEMENTED;
	}

    public function render(res: Res) {
        throw Fault.UNIMPLEMENTED;
    }
}

