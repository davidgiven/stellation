package client;

import commands.CommandDispatcher;
import haxe.Timer;
import interfaces.ITime;
import interfaces.IConsole;
import js.Browser;
import runtime.shared.Time;
import tink.CoreApi;
import utils.Injectomatic.bind;
import utils.Injectomatic.inject;
import utils.Message;
import utils.Random;

class ConsoleImpl implements IConsole {
    public function new(){}

    public function println(s: String): Promise<Noise> {
        trace(s);
        return Noise;
    }
}
        
@await
class Main {
	static function main() {
		Console.start();

        bind(IConsole, new ConsoleImpl());
		bind(CommandDispatcher, new CommandDispatcher());
		bind(ITime, new Time());
		bind(Random, new Random());

		Browser.document.getElementById("loading").remove();

        var c = inject(CommandDispatcher).resolve(["help"]);
        c.run().handle(i -> trace(i));

		var button = Browser.document.createButtonElement();
		button.textContent = "Click me!";
		button.onclick = function(event) {
			Browser.alert("Haxe is great");
		}
		Browser.document.body.appendChild(button);
	}

    @async
    static function hasYield() {
        var i = 0;
        while (true) {
            @await waitForIt();

            var console = inject(IConsole);
            console.println('i = ${i++}');
        }
    }

    static function waitForIt() {
		return Future.async((cb) ->
			Timer.delay(() -> cb(true), 1000));
	}
}
// vim: ts=4 sw=4 et
