package commands;

import commands.AbstractCommand;
import interfaces.IConsole;
import tink.CoreApi;

@await
class PingCommand extends AbstractRemoteCommand {
	public static var REF: CommandRef = { name: "ping", constructor: () -> new PingCommand() };

	public function new() {
		super("pings the server for a status update");
	}
}



