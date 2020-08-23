package commands;

import commands.AbstractCommand;
import interfaces.IConsole;
import tink.CoreApi;

@await
class HelpCommand extends AbstractCommand {
	public static var REF: CommandRef = { name: "help", constructor: () -> new HelpCommand() };

	public function new() {}

	@async public override function run() {
		console.println("Hello, world!");
	}
}

