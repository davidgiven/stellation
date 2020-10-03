package;

import haxe.Exception;
import haxe.PosInfos;
import utest.Test;
import utils.Fault;

class TestCase extends Test {
	function captureFault(cb: Void -> Void): Fault {
		try {
			cb();
		} catch (f: Fault) {
			return f;
		}
		return null;
	}
}

