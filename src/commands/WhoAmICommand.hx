package commands;

import model.SStar;
import tink.CoreApi;
import utils.GetOpt.getopt;
import utils.Flags;

class WhoAmICommand extends AbstractLocalCommand<Noise, Noise> {
    @:keep public static final NAME = "whoami";
    @:keep public static final DESCRIPTION = "shows you who you're logged in as";

    @:keep override function parse(argv: Array<String>): Noise {
		if (argv.length != 1) {
            throw Flags.unrecognisedFlagException(argv[1]);
		}
		return Noise;
    }

    override function run(argv: Array<String>, req: Noise): Noise {
        return Noise;
    }

	override function render(res: Noise): Void {
		console.println('You are ${player.name} (#${player.oid}).');
	}
}


