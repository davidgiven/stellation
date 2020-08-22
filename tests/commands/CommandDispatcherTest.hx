package commands;

import Tests.faultOf;
import commands.CommandDispatcher;
import org.hamcrest.Matchers.assertThat;
import org.hamcrest.Matchers.containsString;
import org.hamcrest.Matchers.equalTo;
import org.hamcrest.Matchers.isEmpty;
import utils.FaultDomain.SYNTAX;
import utils.Flags;
import utils.GetOpt.getopt;

class CommandDispatcherTest extends TestCase {
	var commandDispatcher: CommandDispatcher;

	override function setup() {
		commandDispatcher = new CommandDispatcher();
	}

	function test() {
	}
}

