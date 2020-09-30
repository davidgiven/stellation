package commands;

import tink.CoreApi;
import model.SStar;
import ui.StarWindow;
import utils.Oid;
import utils.Flags;

class ShowStarCommand extends AbstractLocalCommand<Noise> {
    @:keep public static final NAME = "show-star";
    @:keep public static final DESCRIPTION = "displays a star window";

	static var starWindows: Map<SStar, StarWindow> = [];
	var star: SStar = null;

    @:keep override function parse(): Void {
		if (argv.length != 2) {
            throw Flags.unrecognisedFlagException(argv[1]);
		}
		var oid = Std.parseInt(argv[1]);
		if (oid == null) {
            throw Flags.unrecognisedFlagException(argv[1]);
		}
		star = objectLoader.loadObject(oid, SStar);
    }

    override function run(): Noise {
        return argv;
    }

	override function render(res: Noise): Void {
		var w = starWindows[star];
		if (w == null) {
			w = new StarWindow(star);
			w.create();
			starWindows[star] = w;
		}
		w.show();
	}
}

