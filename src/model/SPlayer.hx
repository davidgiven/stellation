package model;

import utils.Fault;
import model.Properties;

@:tink
class SPlayer extends SThing {
	@:sproperty public var username: String;
	@:sproperty public var visibleObjects: ObjectSet<SThing>;
	@:sproperty public var ships: ObjectSet<SShip>;

	public function isGod(): Bool {
		return oid == ObjectLoader.GOD_OID;
	}

	public function checkGod(): Void {
		if (!isGod()) {
			throw Fault.PERMISSION_DENIED;
		}
	}

	public function calculateVisibleObjects(): Iterable<SThing> {
		return [];
	}
}

