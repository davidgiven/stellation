package commands;

import model.SStar;
import tink.CoreApi;
import utils.GetOpt.getopt;
import utils.Flags;

class WhoAmICommand extends AbstractLocalCommand<Noise> {
    @:keep public static final NAME = "whoami";
    @:keep public static final DESCRIPTION = "shows you who you're logged in as";

    @:keep override function parse(): Void {
		if (argv.length != 1) {
            throw Flags.unrecognisedFlagException(argv[1]);
		}
    }

    override function run(): Noise {
        return Noise;
    }

	override function render(res: Noise): Void {
		console.println('You are ${player.name} (#${player.oid}).');
	}
}


