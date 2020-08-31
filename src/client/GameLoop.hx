package client;

import commands.CommandDispatcher;
import interfaces.IConsole;
import runtime.shared.InMemoryDatastore;
import utils.Injectomatic.bind;
import interfaces.IDatastore;
import model.ObjectLoader;
import tink.CoreApi;
import ui.ConsoleWindow;
import ui.LoginForm;
import utils.Fault;
import utils.Injectomatic.inject;

@:tink
@await
class GameLoop implements IConsole {
	var cookies = inject(Cookies);
	var commandDispatcher = inject(CommandDispatcher);

	var consoleWindow: ConsoleWindow = null;

	public function new() {}

	@async
	public function startGame(): Noise {
		doLogin();
		return Noise;
	}

	@async
	public function doLogin(): Noise {
		while (true) {
			//datastore.initialiseDatabase()

			var defaultUsername = cookies.get("username") | if (null) "";
			var defaultPassword = cookies.get("password") | if (null) "";

			var loginData = @await new LoginForm(defaultUsername, defaultPassword).execute();
			if (!loginData.canceled) {
				try {
					//clientInterface.setCredentials(loginData.username!!, loginData.password!!)
					//clock.setTime(0.0)

					//val command = commandDispatcher.resolve(listOf("ping"))
					//command.run()

					cookies.set("username", loginData.username);
					cookies.set("password", loginData.password);
					doGame();
					break;
				} catch (f: Fault) {
					if (f.domain == PERMISSION) {
					//	AlertForm("Login failed", "Username or password unrecognised.").execute()
					} else {
						throw f;
					}
				}
			}
		}
		return Noise;
	}

	@async
	public function doGame(): Noise {
		var datastore = new InMemoryDatastore();
		datastore.initialiseDatabase();
		bind(IDatastore, datastore);

        bind(ObjectLoader, new ObjectLoader());

		consoleWindow = new ConsoleWindow();
		consoleWindow.create();
		consoleWindow.onCommandReceived.handle(onCommandReceived);
		consoleWindow.show();

        @await println("Welcome to Stellation VII.");
        @await println("Try 'help' if you're feeling lucky.");

		return Noise;
	}

	@async
	public function println(s: String): Noise {
		if (consoleWindow != null) {
			consoleWindow.println(s);
		}
		return Noise;
	}

	public function onCommandReceived(command: String): Void {
		commandDispatcher.call(command);
	}
}

