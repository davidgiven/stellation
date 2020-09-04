package commands;

import commands.AbstractCommand;
import interfaces.IConsole;
import tink.CoreApi;
import utils.Flags;

class PingCommand extends AbstractRemoteCommand<Noise, Noise> {
    @:keep public static final NAME = "ping";
    @:keep public static final DESCRIPTION = "pings the server for a status update";

	public function new() {}

    @:keep override function parse(argv: Array<String>): Noise {
        if (argv.length != 1) {
            throw Flags.unrecognisedFlagException(argv[1]);
        }
        return Noise;
    }

    override function run(argv: Array<String>, req: Noise): Noise {
        return Noise;
    }

	override function render(res: Noise) {
	}
}



