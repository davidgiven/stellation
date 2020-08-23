package commands;

import commands.CommandDispatcher;
import utils.FaultDomain.SYNTAX;
import utils.Flags;
import utils.GetOpt.getopt;

class CommandDispatcherTest extends TestCase {
	var commandDispatcher: CommandDispatcher;

	function setup() {
		commandDispatcher = new CommandDispatcher();
	}
}

