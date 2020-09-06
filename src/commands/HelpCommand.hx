package commands;

import commands.AbstractCommand;
import interfaces.IConsole;
import tink.CoreApi;
import utils.Flags;

class HelpCommand extends AbstractLocalCommand<Noise> {
    @:keep public static final NAME = "help";
    @:keep public static final DESCRIPTION = "displays command line help";

    @:keep override function parse(): Void {
        if (argv.length != 1) {
            throw Flags.unrecognisedFlagException(argv[1]);
        }
    }

    override function run(): Noise {
        return Noise;
    }

	override function render(res: Noise) {
		console.println("This is not helpful!");
	}
}

