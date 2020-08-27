package commands;

import commands.AbstractCommand;
import interfaces.IConsole;
import tink.CoreApi;

@await
class EchoCommand extends AbstractLocalCommand {
	public static var REF: CommandRef = { name: "echo", constructor: () -> new EchoCommand() };

	public function new() {
		super("displays a string to the console");
	}

    override function parseRemainingArguments(argv: Array<String>) {
    }

	@async public override function run() {
        console.println(argv.slice(1).join(" "));
	}
}


