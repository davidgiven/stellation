package utils;

import Std.parseInt;
import utils.Fault;
import utils.FaultDomain.SYNTAX;

class AbstractFlag {
	public var name: String;

	public function new(name: String) {
		this.name = name;
	}

	public function set(input: String): Bool {
		return false;
	}
}

class CallbackFlag extends AbstractFlag {
	var callback: (String) -> Void;

	public function new(name: String, callback: (String) -> Void) {
		super(name);
		this.callback = callback;
	}

    public override function set(input: String): Bool {
        callback(input);
        return false;
    }
}

@:generic
class VarFlag<T> extends AbstractFlag {
	var setter: (T) -> Void;

	public function new(name: String, setter: (T) -> Void) {
		super(name);
		this.setter = setter;
	}

	function translate(input: String): T {
		throw Fault.UNIMPLEMENTED;
	}

    public override function set(input: String): Bool {
        var value = translate(input);
		setter(value);
        return true;
    }
}

class BooleanFlag extends VarFlag<Bool> {
	public function new(name: String, setter: (Bool) -> Void) {
		super(name, setter);
	}

	override function translate(input: String): Bool {
		switch (input) {
			case "true": return true;
			case "false": return false;
		}
		throw Flags.invalidFlagValueException(input);
	}
}

class IntFlag extends VarFlag<Int> {
	public function new(name: String, setter: (Int) -> Void) {
		super(name, setter);
	}

	override function translate(input: String): Int {
		var i = parseInt(input);
		if (i == null) {
			throw Flags.invalidFlagValueException(input);
		}
		return i;
	}
}

class StringFlag extends VarFlag<String> {
	public function new(name: String, setter: (String) -> Void) {
		super(name, setter);
	}

	override function translate(input: String): String {
		return input;
	}
}

class Flags {
	public static function duplicateFlagException(arg: String): Fault {
		return new Fault(SYNTAX).withDetail('flag \'$arg\' is already defined');
	}

	public static function missingFlagException(arg: String): Fault {
		return new Fault(SYNTAX).withDetail('parameter for flag \'$arg\' is missing (try --help)');
	}

	public static function unrecognisedFlagException(arg: String): Fault {
		return new Fault(SYNTAX).withDetail('unrecognised parameter \'$arg\' (try --help)');
	}

	public static function invalidFlagValueException(arg: String): Fault {
		return new Fault(SYNTAX).withDetail('invalid value for flag \'$arg\' (try --help)');
	}

	public static function expectParameters(wanted: Int, got: Int): Fault {
		return new Fault(SYNTAX).withDetail('expected $wanted parameters but got $got');
	}

    public var map: Map<String, AbstractFlag> = [];
	public var remainingSetter: Iterable<String> -> Void = null;
	public var expectRemaining: Int = 0;

	public function new() {}

    public function add(flag: AbstractFlag): Flags {
        if (map.exists(flag.name)) {
            throw duplicateFlagException(flag.name);
        }
		map[flag.name] = flag;
        return this;
    }

    public function addFlag(name: String, callback: (String) -> Void): Flags {
        return add(new CallbackFlag(name, callback));
	}

    public function addBoolean(name: String, setter: (Bool) -> Void): Flags {
        return add(new BooleanFlag(name, setter));
	}

    public function addInt(name: String, setter: (Int) -> Void): Flags {
        return add(new IntFlag(name, setter));
	}

    public function addString(name: String, setter: (String) -> Void): Flags {
        return add(new StringFlag(name, setter));
	}

	public function withRemaining(count: Int, setter: Iterable<String> -> Void): Flags {
		expectRemaining = count;
		remainingSetter = setter;
		return this;
	}
}


