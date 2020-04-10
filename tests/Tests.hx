package;

import haxe.unit.TestRunner;
import utils.Exception;
import utils.Fault;

class Tests {
	public static function main() {
		var r = new TestRunner();
		r.add(new utils.InjectomaticTest());
		r.add(new utils.ArgifierTest());
		r.run();
	}
		
	public static function exceptionOf(fn: Void->Void): Exception {
		try {
			fn();
		} catch (e: Exception) {
			return e;
		}
		return null;
	}
		
	public static function faultOf(fn: Void->Void): Fault {
		try {
			fn();
		} catch (e: Fault) {
			return e;
		}
		return null;
	}
}

