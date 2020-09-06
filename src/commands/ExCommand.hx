package commands;

import commands.AbstractCommand;
import interfaces.IConsole;
import tink.CoreApi;
import model.SThing;
import utils.Oid;
import utils.Flags;

class ExCommand extends AbstractLocalCommand<Noise, Noise> {
    @:keep public static final NAME = "ex";
    @:keep public static final DESCRIPTION = "examines an object";

	private var oid: Null<Oid> = null;

    @:keep override function parse(argv: Array<String>): Noise {
		if (argv.length != 2) {
            throw Flags.unrecognisedFlagException(argv[1]);
		}
		oid = Std.parseInt(argv[1]);
		if (oid == null) {
            throw Flags.unrecognisedFlagException(argv[1]);
		}
			
        return Noise;
    }

    override function run(argv: Array<String>, req: Noise): Noise {
        return req;
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

