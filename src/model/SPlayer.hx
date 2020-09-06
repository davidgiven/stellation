package model;

import utils.Fault;
import model.Properties;
import tink.CoreApi;
using utils.ArrayTools;
using model.ObjectSetTools;

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

	public function calculateVisibleStars(): Map<SStar, Noise> {
		var set = new Map<SStar, Noise>();
		for (ship in ships.getAll()) {
			if (ship.findChild(SJumpdrive) != null) {
				var star = ship.getContainingStar();
				if (star != null) {
					set[star] = Noise;
				}
			}
		}
		return set;
	}

	public function calculateVisibleObjects(): Map<SThing, Noise> {
		var set = new Map<SThing, Noise>();
		set[this] = Noise;
		for (star => n in calculateVisibleStars()) {
			set.addMap(star.calculateHierarchicalContents());
		}
		return set;
	}

	public function canSee(object: SThing): Bool {
		return visibleObjects.exists(object);
	}
}

