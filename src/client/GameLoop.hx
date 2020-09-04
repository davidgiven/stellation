package client;

import commands.CommandDispatcher;
import commands.PingCommand;
import interfaces.IConsole;
import interfaces.IDatastore;
import interfaces.IRemoteClient;
import model.ObjectLoader;
import runtime.shared.InMemoryDatastore;
import tink.CoreApi;
import ui.ConsoleWindow;
import ui.LoginForm;
import utils.Fault;
import utils.Injectomatic.bind;
import utils.Injectomatic.inject;

@:tink
@await
class GameLoop implements IConsole {
	var cookies = inject(Cookies);
	@:calc var commandDispatcher = inject(CommandDispatcher);
	@:calc var remoteClient = inject(IRemoteClient);

	var consoleWindow: ConsoleWindow = null;
	@:signal var onTerminateGame: Noise;

	public function new() {}

	@await public function execute() {
		bind(IRemoteClient, new RemoteClient());
		bind(CommandDispatcher, new CommandDispatcher());

		while (true) {
			/* Attempt to log in */

			while (true) {
				var defaultUsername = cookies.get("username") | if (null) "";
				var defaultPassword = cookies.get("password") | if (null) "";

				var loginData = @await new LoginForm(defaultUsername, defaultPassword).execute();
				if (!loginData.canceled) {
					try {
						remoteClient.setCredentials(loginData.username, loginData.password);
						//clock.setTime(0.0)

						cookies.set("username", loginData.username);
						cookies.set("password", loginData.password);
						@await new PingCommand().callAsync(["ping"]);

						break;
					} catch (f: Fault) {
						trace("caught error:", f);
//						if (f.domain == PERMISSION) {
//						//	AlertForm("Login failed", "Username or password unrecognised.").execute()
//						} else {
//							throw f;
//						}
					} catch (f) {
						trace("uncaught error:", f);
					}
				}
			}

			/* Actually run the game */

			var datastore = new InMemoryDatastore();
			datastore.initialiseDatabase();
			bind(IDatastore, datastore);

			bind(ObjectLoader, new ObjectLoader());

			consoleWindow = new ConsoleWindow();
			consoleWindow.create();
			consoleWindow.onCommandReceived.handle(onCommandReceived);
			consoleWindow.show();

			println("Welcome to Stellation VII.");
			println("Try 'help' if you're feeling lucky.");

			@await onTerminateGame.next();
		}
	}

	public function terminateGame() {
		_onTerminateGame.trigger(Noise);
	}

	public function println(s: String): Void {
		if (consoleWindow != null) {
			consoleWindow.println(s);
		}
	}

	public function onCommandReceived(command: String): Void {
		commandDispatcher.clientCall(command);
	}
}

