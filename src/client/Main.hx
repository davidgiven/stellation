package client;

import commands.CommandDispatcher;
import interfaces.ICommandDispatcher;
import interfaces.ITime;
import js.Browser;
import runtime.shared.Time;
import utils.Injectomatic.bind;
import utils.Injectomatic.inject;
import utils.Random;
import utils.Message;

class Main {
	static function main() {
		Console.start();

		bind(ICommandDispatcher, new CommandDispatcher());
		bind(ITime, new Time());
		bind(Random, new Random());

		Browser.document.getElementById("loading").remove();

		var m = new Message();

		var button = Browser.document.createButtonElement();
		button.textContent = "Click me!";
		button.onclick = function(event) {
			Browser.alert("Haxe is great");
		}
		Browser.document.body.appendChild(button);
	}
}
// vim: ts=4 sw=4 et
