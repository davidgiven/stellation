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
		HelpCommand,
		PingCommand,
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

	public function resolve(argv: Array<String>): AbstractCommand<Dynamic, Dynamic> {
		var name = argv[0];
		var record = commands[name];
		if (record == null) {
			throw new Fault(SYNTAX).withDetail('command \'${name}\' not found');
		}
		return record.klass.createInstance([]);
	}

	@async public function call(cmdline: String): Noise {
		try {
			@await console.println('> ${cmdline}');
			var argv = argify(cmdline);
			callArgv(argv);
        } catch (f: Fault) {
            console.println('Failed: ${f.detail}');
        } catch (e) {
            console.println('Internal error: $e');
        }
		return Noise;
	}

	@async public function callArgv(argv: Array<String>): Noise {
		if (argv.length == 0) {
			return Noise;
		}

		var command = resolve(argv);
		var req = command.parse(argv);
		var res = @await command.run(argv, req);
		command.render(res);
		return Noise;
	}

//    override val commands: Map<String, () -> AbstractCommand> by lazy { populateCommands() }
//
//    fun populateCommands(): Map<String, () -> AbstractCommand> {
//        var commands: Map<String, () -> AbstractCommand> = emptyMap()
//
//        val commandsList = listOf(
//                ::HelpCommand,
//                ::EchoCommand,
//                ::PingCommand,
//                ::SetPasswordCommand,
//                ::WhoAmICommand,
//                ::StarsCommand,
//                ::ExCommand,
//                ::RenameCommand
//        )
//
//        for (c in commandsList) {
//            val name = c().name
//            check(name !in commands)
//            commands += name to c
//        }
//
//        return commands
//    }
//
//    override fun resolve(argv: List<String>): AbstractCommand {
//        val name = argv[0]
//        val constructor = commands.get(name) ?: throwCommandNotFoundException(name)
//        val command = constructor()
//        command.parseArguments(argv)
//        command.output.setSuccess(false)
//        return command
//    }
}


