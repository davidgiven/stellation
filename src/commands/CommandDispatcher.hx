package commands;

import utils.Fault;
import utils.FaultDomain.SYNTAX;
import interfaces.IConsole;
import utils.Injectomatic.inject;
import utils.Argifier.argify;
import utils.Argifier.unargify;
import tink.CoreApi;
using Type;

typedef CommandRecord = {
	name: String,
	description: String,
	klass: Class<Any>
};

@:tink
@await
class CommandDispatcher {
	private static final COMMANDS: Array<Dynamic> = [
		EchoCommand,
		ExCommand,
		HelpCommand,
		PingCommand,
		RenameCommand,
		ShipsCommand,
		StarsCommand,
		WhoAmICommand,
	];

	public var commands: Map<String, CommandRecord> = [];
	@:lazy public var console = inject(IConsole);

	public function new() {
		for (ref in COMMANDS) {
			var name = Reflect.field(ref, "NAME");
			var description = Reflect.field(ref, "DESCRIPTION");
			commands[name] = {
				name: name,
				description: description,
				klass: cast(ref, Class<Dynamic>)
			};
		}
	}

	public function resolve(argv: Array<String>): AbstractCommand<Dynamic> {
		var name = argv[0];
		var record = commands[name];
		if (record == null) {
			throw new Fault(SYNTAX).withDetail('command \'${name}\' not found');
		}
		var command: AbstractCommand<Dynamic> = record.klass.createInstance([]);
		command.setArgv(argv);
		return command;
	}

	@async public function clientCall(cmdline: String): Noise {
		try {
			console.println('> ${cmdline}');
			var argv = argify(cmdline);
			if (argv.length == 0) {
				return Noise;
			}

			var command = resolve(argv);
			@await command.callAsync();
        } catch (f: Fault) {
            console.println('Failed: ${f.detail}');
        } catch (e) {
            console.println('Internal error: $e');
        }
		return Noise;
	}

	public function serverCall(argv: Array<String>): Void {
		var command = resolve(argv);
		command.callSync();
	}

	public function remoteCall(argv: Array<String>): Dynamic {
		var command = resolve(argv);
		return command.callRemote();
	}
}


