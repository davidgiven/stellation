package client;

import commands.CommandDispatcher;
import commands.PingCommand;
import interfaces.IConsole;
import interfaces.IDatastore;
import interfaces.IRemoteClient;
import model.ObjectLoader;
import model.Syncer;
import model.SUniverse;
import model.SGalaxy;
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
	@:calc var datastore = inject(IDatastore);
	@:calc var objectLoader = inject(ObjectLoader);

	var consoleWindow: ConsoleWindow = null;
	@:signal var onTerminateGame: Noise;

	public function new() {}

	@await public function execute() {
		while (true) {
			/* Attempt to log in */

			while (true) {
				var defaultUsername = cookies.get("username") | if (null) "";
				var defaultPassword = cookies.get("password") | if (null) "";

				var loginData = @await new LoginForm(defaultUsername, defaultPassword).execute();
				if (!loginData.canceled) {
					try {
						//clock.setTime(0.0)

						bind(IDatastore, new InMemoryDatastore());
						datastore.initialiseDatabase();

						bind(ObjectLoader, new ObjectLoader());
						objectLoader.initialiseProperties();

						bind(Syncer, new Syncer());

						bind(IRemoteClient, new RemoteClient());
						remoteClient.setCredentials(loginData.username, loginData.password);

						bind(CommandDispatcher, new CommandDispatcher());

						cookies.set("username", loginData.username);
						cookies.set("password", loginData.password);
						@await new PingCommand().callAsync(["ping"]);

						bind(SUniverse, objectLoader.findUniverse()); 
						bind(SGalaxy, objectLoader.findGalaxy()); 

						break;
					} catch (f: Fault) {
						trace("caught error:", f);
//						if (f.domain == PERMISSION) {
//						//	AlertForm("Login failed", "Username or password unrecognised.").execute()
//						} else {
//							throw f;
//						}
					} catch (d: Dynamic) {
						trace("uncaught error:", d);
						throw d;
					}
				}
			}

			/* Actually run the game */

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

	@await
	public function onCommandReceived(command: String): Void {
		@await commandDispatcher.clientCall(command);
		consoleWindow.setReady();
	}
}

