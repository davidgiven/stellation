package commands;

import interfaces.IConsole;
import utils.GetOpt.getopt;
import utils.Injectomatic.inject;
import utils.Flags;
import utils.Message;
import utils.Fault;
import utils.FaultDomain.SYNTAX;
import tink.CoreApi;
using utils.NullTools;

typedef CommandRef = { name: String, constructor: () -> AbstractCommand };

abstract CommandMessage(Message) {
	private static var SUCCESS = "_success";

	public function new() {
		this = new Message();
	}

	public function setSuccess(success: Bool): Void this.setBool(SUCCESS, success);
	public function getSuccess(): Bool              return this.getBool(SUCCESS).or(false);
}

@await
class AbstractCommand {
    var console = inject(IConsole);

    var description: String;
    var flags: Flags;

    public var argv: Array<String> = [];
    var input: CommandMessage = new CommandMessage();
    var output: CommandMessage = new CommandMessage();

	public function parseArguments(argv: Array<String>) {
		this.argv = argv;
		output.setSuccess(false);
		var remaining = getopt(argv.slice(1), flags);
		parseRemainingArguments(remaining);
		validateArguments();
	}

	public function parseRemainingArguments(argv: Array<String>) {
		if (argv.length != 0) {
			throw new Fault(SYNTAX).withDetail('unrecognised arguments starting with \'${argv[0]}\'');
        }
    }

	public function validateArguments() {}

	@async public function run() {
		output.setSuccess(true);
	}
}

