package commands;

import commands.AbstractCommand;
import interfaces.IConsole;
import tink.CoreApi;
import model.SThing;
import utils.Oid;
import utils.Flags;

class ExCommand extends AbstractLocalCommand<Noise> {
    @:keep public static final NAME = "ex";
    @:keep public static final DESCRIPTION = "examines an object";

	private var oid: Null<Oid> = null;

    @:keep override function parse(): Void {
		if (argv.length != 2) {
            throw Flags.unrecognisedFlagException(argv[1]);
		}
		if (argv[1] == "me") {
			oid = player.oid;
		} else {
			oid = Std.parseInt(argv[1]);
		}
		if (oid == null) {
            throw Flags.unrecognisedFlagException(argv[1]);
		}
    }

    override function run(): Noise {
        return Noise;
    }

	override function render(res: Noise): Void {
		var obj = objectLoader.loadObject(oid, SThing);
		for (property in objectLoader.getAllProperties()) {
			if (datastore.hasProperty(oid, property.name)) {
				var value = property.getDynamicValue(obj);
				if (value != null) {
					console.println('#${oid}.${property.name}: ${value}');
				}
			}
		}
	}
}

