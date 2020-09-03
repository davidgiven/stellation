package model;

import utils.Fault;

@:tink
class SPlayer extends SThing {
	public function isGod(): Bool {
		return oid == ObjectLoader.GOD_OID;
	}

	public function checkGod(): Void {
		if (!isGod()) {
			throw Fault.PERMISSION_DENIED;
		}
	}
}

