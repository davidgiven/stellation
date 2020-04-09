package;

import haxe.unit.TestRunner;
import utils.Exception;

class Tests {
	public static function main() {
		var r = new TestRunner();
		r.add(new utils.InjectomaticTest());
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
}


