package commands

import interfaces.IConsole
import utils.get

/* Commands which can only be called on the client (typically UI-related). */
abstract class AbstractClientCommand: AbstractCommand() {
}
