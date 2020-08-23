package;

import utest.UTest;
import utils.Exception;
import utils.Fault;

class Tests {
	public static function main() {
		UTest.run([
			new commands.CommandDispatcherTest(),
			new utils.ArgifierTest(),
			new utils.GetOptTest(),
			new utils.InjectomaticTest(),
			new utils.MessageTest(),
		]);
//
//		var r = new TestRunner();
//		r.add(new commands.CommandDispatcherTest());
//		r.run();
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


