package commands;

import commands.AbstractCommand;
import interfaces.IConsole;
import tink.CoreApi;

@await
class HelpCommand extends AbstractLocalCommand {
	public static var REF: CommandRef = { name: "help", constructor: () -> new HelpCommand() };

	public function new() {
		super("displays command line help");
	}

	@async public override function run() {
		console.println("Hello, world!");
	}
}

