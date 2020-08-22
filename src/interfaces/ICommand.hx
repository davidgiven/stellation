package interfaces;

import utils.Fault;
import utils.FaultDomain.SYNTAX;
import utils.Flags;
//import utils.Message;
//

class Exceptions {
	public static function throwCommandNotFoundException(name: String): Void {
		throw new Fault(SYNTAX).withDetail("command '$name' not found");
	}

	public static function throwCommandSyntaxException(message: String): Void {
		throw new Fault(SYNTAX).withDetail(message);
	}
}

//class CommandMessage extends Message {
//	public function new() {}
//	public function new(serialised: String) { super(serialised); }
//
//    public function setSuccess(success: Boolean) {
//		setBoolean(SUCCESS, success);
//	}
//
//    public function getSuccess(): Boolean {
//		return getBooleanOrDefault(SUCCESS, false);
//	}
//}

interface ICommand {
//	private var SUCCESS = "_success";
//
//    var name: String;
//    var description: String;
//    var flags: Flags;
//
//    var argv: List<String>;
//    var input: CommandMessage;
//    var output: CommandMessage;
//
//    public function parseArguments(argv: List<String>);
//    public function parseRemainingArguments(argv: List<String>);
//    public function validateArguments();
//    public function run();
//    public function serverRun();
//    public function renderResult();

}

