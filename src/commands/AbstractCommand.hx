package commands;

import interfaces.IConsole;
import utils.GetOpt.getopt;
import utils.Injectomatic.inject;
import utils.Flags;
import utils.Message;
import utils.Fault;
import utils.FaultDomain.SYNTAX;
using utils.NullTools;

typedef CommandRef = { name: String, constructor: () -> AbstractCommand };

abstract CommandMessage(Message) {
	private static var SUCCESS = "_success";

	public function setSuccess(success: Bool): Void this.setBool(SUCCESS, success);
	public function getSuccess(): Bool              return this.getBool(SUCCESS).or(false);
}

@await
class AbstractCommand {
    var console = inject(IConsole);

    var description: String;
    var flags: Flags;

    public var argv: Array<String>;
    var input: CommandMessage;
    var output: CommandMessage;


	public function parseArguments(argv: Array<String>) {
		this.argv = argv;
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

    public function serverRun() {
		throw new Fault(SYNTAX).withDetail("this command can't be used on the server");
    }

    public function renderResult() {
        console.println("OK");
    }


}

