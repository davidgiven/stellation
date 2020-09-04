package commands;

import interfaces.IConsole;
import utils.GetOpt.getopt;
import utils.Injectomatic.inject;
import utils.Flags;
import utils.Fault;
import utils.FaultDomain.SYNTAX;
import tink.CoreApi;
using utils.NullTools;

@await
class AbstractCommand<Req, Res> {
    var console = inject(IConsole);

	@async public function callAsync(argv: Array<String>): Noise {
		callSync(argv);
		return Noise;
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

