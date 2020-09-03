package commands;

import utils.Fault;
import utils.FaultDomain.SYNTAX;
import interfaces.IConsole;
import utils.Injectomatic.inject;
import utils.Argifier.argify;
import utils.Argifier.unargify;
import tink.CoreApi;

@:tink
@async
class CommandDispatcher {
	private static final COMMANDS = [
		EchoCommand.REF,
		HelpCommand.REF,
		PingCommand.REF,
	];

	public var commands: Map<String, () -> AbstractCommand> = [];
	@:lazy public var console = inject(IConsole);

	public function new() {
		for (ref in COMMANDS) {
			commands[ref.name] = ref.constructor;
		}
	}

	public function resolve(argv: Array<String>): AbstractCommand {
		var name = argv[0];
		var commandConstructor = commands[name];
		if (commandConstructor == null) {
			throw new Fault(SYNTAX).withDetail('command \'${name}\' not found');
		}
		var command = commandConstructor();
		command.parseArguments(argv);
		return command;
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
		@await command.run();
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


