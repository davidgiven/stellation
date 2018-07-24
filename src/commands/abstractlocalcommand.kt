package commands

import interfaces.IConsole
import utils.get

/* Commands which can be run from anywhere, and don't require server interaction. */
abstract class AbstractLocalCommand: AbstractCommand() {
}
