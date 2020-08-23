package utils;

import utils.Fault;
import utils.FaultDomain.INTERNAL;

class Exception {
	var msg: String;

	public function new(msg: String) {
		this.msg = msg;
	}

	public function toString(): String {
		return '${Type.getClass(this)}: ${msg}';
	}

	public static function throwInvalidCodecDataException(s: String, ?e: Exception): Void
        throw new Fault(INTERNAL).withDetail('invalid encoded packet: $s');
}

