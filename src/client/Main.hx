package client;

import commands.CommandDispatcher;
import haxe.Timer;
import interfaces.IConsole;
import interfaces.IDatastore;
import interfaces.ITime;
import interfaces.IUi;
import js.Browser;
import runtime.js.JsUi;
import runtime.shared.Time;
import tink.CoreApi;
import ui.ConsoleWindow;
import utils.Injectomatic.bind;
import utils.Injectomatic.inject;
import utils.Message;
import utils.Random;

class Main {
	static function main() {
		Console.start();

		bind(CommandDispatcher, new CommandDispatcher());
		bind(ITime, new Time());
		bind(Random, new Random());
        bind(IUi, new JsUi());
        bind(Cookies, new Cookies());

        var gameloop = new GameLoop();
        bind(GameLoop, gameloop);
        bind(IConsole, gameloop);

		Browser.document.getElementById("loading").remove();
        gameloop.startGame();
	}
}
// vim: ts=4 sw=4 et
