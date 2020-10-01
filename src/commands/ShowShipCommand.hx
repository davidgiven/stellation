package commands;

import tink.CoreApi;
import model.SShip;
import ui.ShipWindow;
import utils.Oid;
import utils.Flags;

class ShowShipCommand extends AbstractLocalCommand<Noise> {
    @:keep public static final NAME = "show-ship";
    @:keep public static final DESCRIPTION = "displays a ship window";

	static var shipWindows: Map<SShip, ShipWindow> = [];
	var ship: SShip = null;

    @:keep override function parse(): Void {
		if (argv.length != 2) {
            throw Flags.unrecognisedFlagException(argv[1]);
		}
		var oid = Std.parseInt(argv[1]);
		if (oid == null) {
            throw Flags.unrecognisedFlagException(argv[1]);
		}
		ship = objectLoader.loadObject(oid, SShip);
    }

    override function run(): Noise {
        return argv;
    }

	override function render(res: Noise): Void {
		var w = shipWindows[ship];
		if (w == null) {
			w = new ShipWindow(ship);
			w.create();
			shipWindows[ship] = w;
		}
		w.show();
	}
}

