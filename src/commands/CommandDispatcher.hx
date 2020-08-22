package commands;

import interfaces.ICommandDispatcher;
import interfaces.ICommand;
import interfaces.ICommand.Exceptions.throwCommandNotFoundException;

class CommandDispatcher implements ICommandDispatcher {
	public var commands: Map<String, () -> AbstractCommand> = [];

	public function new() {
		for (ref in [HelpCommand.REF]) {
			commands[ref.name] = ref.constructor;
		}
	}

	public function resolve(argv: Array<String>): ICommand {
		var name = argv[0];
		var commandConstructor = commands[name];
		if (commandConstructor == null) {
			throwCommandNotFoundException(name);
		}
		var command = commandConstructor();
		//command.parseArguments(argc);
		return command;
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


