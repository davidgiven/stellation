package interfaces

interface ICommandDispatcher {
    val commands: Map<String, () -> ICommand>

    fun resolve(cmdline: String): ICommand
    fun resolve(argv: List<String>): ICommand
}
