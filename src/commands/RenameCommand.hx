package commands;

import commands.AbstractCommand;
import interfaces.IConsole;
import tink.CoreApi;
import model.SThing;
import utils.Flags;
import utils.Oid;

class RenameCommand extends AbstractRemoteCommand<Noise> {
    @:keep public static final NAME = "rename";
    @:keep public static final DESCRIPTION = "renames something";

	private var oid: Null<Oid>;
	private var newName: String;

	public function new() {
		super();
	}

    @:keep override function parse(): Void {
        if (argv.length != 3) {
            throw Flags.unrecognisedFlagException(argv[1]);
        }
		oid = Std.parseInt(argv[1]);
		if (oid == null) {
            throw Flags.unrecognisedFlagException(argv[1]);
		}
		newName = argv[2];
    }

    override function run(): Noise {
        var o = objectLoader.loadObject(oid, SThing);
        o.checkModificationAccess();
        o.name = newName;
        return Noise;
    }

	override function render(res: Noise) {
	}
}




