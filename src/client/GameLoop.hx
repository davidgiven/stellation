package client;

import interfaces.IConsole;
import tink.CoreApi;
import ui.ConsoleWindow;
import ui.LoginForm;
import utils.Fault;

@await
class GameLoop implements IConsole {
	var consoleWindow: ConsoleWindow = null;

	public function new() {}

	@async
	public function startGame(): Void {
		doLogin();
	}

	@async
	public function doLogin(): Void {
		while (true) {
			//datastore.initialiseDatabase()

			var defaultUsername = "";
			var defaultPassword = "";
			//val defaultUsername = cookies["username"] ?: ""
			//val defaultPassword = cookies["password"] ?: ""

			var loginData = @await new LoginForm(defaultUsername, defaultPassword).execute();
			if (!loginData.canceled) {
				try {
					//clientInterface.setCredentials(loginData.username!!, loginData.password!!)
					//clock.setTime(0.0)

					//val command = commandDispatcher.resolve(listOf("ping"))
					//command.run()

					//cookies["username"] = loginData.username
					//cookies["password"] = loginData.password
					//doGame()
					//break
				} catch (f: Fault) {
					//if (f.domain == PERMISSION) {
					//	AlertForm("Login failed", "Username or password unrecognised.").execute()
					//} else {
						throw f;
					//}
				}
			}
		}
	}

	@async
	public function println(s: String): Noise {
		if (consoleWindow != null) {
			consoleWindow.println(s);
		}
	}
}

