package commands;

import commands.AbstractCommand;
import interfaces.IConsole;
import tink.CoreApi;
import model.SThing;
import utils.Flags;
import utils.Oid;

class RenameCommand extends AbstractRemoteCommand<Noise, Noise> {
    @:keep public static final NAME = "rename";
    @:keep public static final DESCRIPTION = "renames something";

	private var oid: Null<Oid>;
	private var newName: String;

	public function new() {
		super();
	}

    @:keep override function parse(argv: Array<String>): Noise {
        if (argv.length != 3) {
            throw Flags.unrecognisedFlagException(argv[1]);
        }
		oid = Std.parseInt(argv[1]);
		if (oid == null) {
            throw Flags.unrecognisedFlagException(argv[1]);
		}
		newName = argv[2];
        return Noise;
    }

    override function run(argv: Array<String>, req: Noise): Noise {
        var o = objectLoader.loadObject(oid, SThing);
        o.checkModificationAccess();
        o.name = newName;
        return Noise;
    }

	override function render(res: Noise) {
	}
}




