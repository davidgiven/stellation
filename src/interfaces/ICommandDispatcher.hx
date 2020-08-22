package interfaces;

interface ICommandDispatcher {
    function resolve(argv: Array<String>): ICommand;
}

