package client;

import commands.CommandDispatcher;
import commands.PingCommand;
import interfaces.IConsole;
import interfaces.IDatastore;
import interfaces.IRemoteClient;
import interfaces.IClock;
import model.ObjectLoader;
import model.Syncer;
import model.SUniverse;
import model.SGalaxy;
import runtime.shared.InMemoryDatastore;
import tink.CoreApi;
import ui.ConsoleWindow;
import ui.SummaryWindow;
import ui.LoginForm;
import haxe.io.BytesOutput;
import haxe.Exception;
import utils.Fault;
import utils.Injectomatic.rebind;
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
	var summaryWindow: SummaryWindow = null;
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

						rebind(IDatastore, new InMemoryDatastore());
						datastore.initialiseDatabase();

						rebind(ObjectLoader, new ObjectLoader());
						objectLoader.initialiseProperties();

						rebind(Syncer, new Syncer());
						rebind(IClock, new ClientClock());

						rebind(IRemoteClient, new RemoteClient());
						remoteClient.setCredentials(loginData.username, loginData.password);

						rebind(CommandDispatcher, new CommandDispatcher());

						cookies.set("username", loginData.username);
						cookies.set("password", loginData.password);
						@await new PingCommand().setArgv(["ping"]).callAsync();

						rebind(SUniverse, objectLoader.findUniverse()); 
						rebind(SGalaxy, objectLoader.findGalaxy()); 

						break;
					} catch (f: Fault) {
						if (f.domain == PERMISSION) {
//						//	AlertForm("Login failed", "Username or password unrecognised.").execute()
						} else {
							var b = new BytesOutput();
							b.writeString('${f.detail}\n');
							f.dumpStackTrace(b);
							trace(b.getBytes().toString());
						}
					} catch (d: Exception) {
						var f = new Fault().withException(d);
						var b = new BytesOutput();
						b.writeString('${f.detail}\n');
						f.dumpStackTrace(b);
						trace(b.getBytes().toString());
					}
				}
			}

			/* Actually run the game */

			consoleWindow = new ConsoleWindow();
			consoleWindow.create();
			consoleWindow.onCommandReceived.handle(onCommandReceived);
			consoleWindow.show();

			summaryWindow = new SummaryWindow();
			summaryWindow.create().show();

			var updateWindowSizes = n -> {
				var pos = consoleWindow.getPosition();
				var size = summaryWindow.getSize();
				summaryWindow.setSize(size.x, pos.y-10);
			};

			consoleWindow.onGeometryChange.handle(updateWindowSizes);
			updateWindowSizes(Noise);

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

