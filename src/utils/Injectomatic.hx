package utils;
import haxe.ds.ObjectMap;
import tink.CoreApi;

class InjectomaticException extends Exception {
	public function new(msg: String) {
		super(msg);
	}
}

class Injectomatic {
	private static var injections = new ObjectMap<Dynamic, Dynamic>();

	public static function resetBindingsForTest(): Void {
		injections.clear();
	}

	static function checkBindingDoesNotExist(t: Class<Dynamic>): Void {
		if (injections.exists(t)) {
			throw new InjectomaticException('binding for type ${t} already exists');
		}
	}

	static function checkBindingExists(t: Class<Dynamic>): Void {
		if (!injections.exists(t)) {
			throw new InjectomaticException('no binding for type ${t} exists');
		}
	}

	@:generic
	public static function inject<T>(t: Class<T>): T {
		checkBindingExists(t);
		return injections.get(t);
	}

	@:generic
	public static function injectLazy<T>(t: Class<T>): Lazy<T> {
        return Lazy.ofFunc(
            () -> {
                checkBindingExists(t);
                return injections.get(t);
            }
        );
	}

	@:generic
	public static function bind<T>(t: Class<T>, value: T) {
		checkBindingDoesNotExist(t);
		injections.set(t, value);
	}
}

// vim: ts=4 sw=4 et

