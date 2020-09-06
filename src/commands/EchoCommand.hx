package commands;

import commands.AbstractCommand;
import interfaces.IConsole;
import tink.CoreApi;

typedef Args = Array<String>;

class EchoCommand extends AbstractLocalCommand<Args> {
    @:keep public static final NAME = "echo";
    @:keep public static final DESCRIPTION = "displays a string on the console";

    @:keep override function parse(): Void {
    }

    override function run(): Args {
        return argv;
    }

	override function render(res: Args): Void {
        console.println(res.slice(1).join(" "));
	}
}


