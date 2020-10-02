package;

import utest.UTest;
import haxe.Exception;
import utils.Fault;

class Tests {
	public static function main() {
		UTest.run([
			new runtime.shared.InMemoryDatastoreTest(),
			new runtime.shared.SqlDatastoreTest(),
			new utils.ArgifierTest(),
			new utils.GetOptTest(),
			new utils.InjectomaticTest(),
            new model.InMemoryObjectsTest(),
            new model.SqlObjectsTest(),
            new model.SyncerTest(),
			new model.TimersTest(),
            new runtime.cpp.SqliteTest(),
		]);
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


