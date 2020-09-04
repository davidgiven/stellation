package commands;

import commands.AbstractCommand;
import interfaces.IConsole;
import tink.CoreApi;
import utils.Flags;

class HelpCommand extends AbstractLocalCommand<Noise, Noise> {
    @:keep public static final NAME = "help";
    @:keep public static final DESCRIPTION = "displays command line help";

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
		console.println("This is not helpful!");
	}
}

