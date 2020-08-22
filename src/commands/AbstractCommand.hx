package commands;

import interfaces.ICommand;

typedef CommandRef = { name: String, constructor: () -> AbstractCommand };

class AbstractCommand implements ICommand {
}

