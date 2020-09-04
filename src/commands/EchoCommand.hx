package commands;

import commands.AbstractCommand;
import interfaces.IConsole;
import tink.CoreApi;

typedef Args = Array<String>;

class EchoCommand extends AbstractLocalCommand<Args, Args> {
    @:keep public static final NAME = "echo";
    @:keep public static final DESCRIPTION = "displays a string on the console";

    @:keep override function parse(argv: Array<String>): Args {
        return argv;
    }

    override function run(argv: Array<String>, req: Args): Args {
        return req;
    }

	override function render(res: Args): Void {
        console.println(res.slice(1).join(" "));
	}
}


