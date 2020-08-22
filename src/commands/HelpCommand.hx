package commands;

import commands.AbstractCommand;

class HelpCommand extends AbstractCommand {
	public static var REF: CommandRef = { name: "help", constructor: () -> new HelpCommand() };

	public function new() {}

}

