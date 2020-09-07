package commands;

import commands.AbstractCommand;
import interfaces.IConsole;
import tink.CoreApi;
import utils.Flags;

class PingCommand extends AbstractRemoteCommand<Noise> {
    @:keep public static final NAME = "ping";
    @:keep public static final DESCRIPTION = "pings the server for a status update";

	public function new() {
		super();
	}

    override function run(): Noise {
        return Noise;
    }

	override function render(res: Noise) {
	}
}



