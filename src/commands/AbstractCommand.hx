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

	public function parse(argv: Array<String>): Req {
        throw Fault.UNIMPLEMENTED;
	}

	@async public function run(argv: Array<String>, req: Req): Res {
        throw Fault.UNIMPLEMENTED;
	}

    public function render(res: Res) {
        throw Fault.UNIMPLEMENTED;
    }
}

