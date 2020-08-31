package model;

import utils.Fault;
import utils.FaultDomain;

class ThingTools {
	public static function remove<T: SThing>(thing: T): T {
        if (thing.location != null) {
            thing.location.contents.remove(thing);
        }
		thing.location = null;
		return thing;
	}

	public static function moveTo<T: SThing>(thing: T, destination: SThing): T {
		var loc = destination;
		while (loc != null) {
			if (loc == thing) {
				throw new Fault(INVALID_ARGUMENT).withDetail('${thing.oid} cannot be contained by itself');
			}
			loc = loc.location;
		}

		remove(thing);
		thing.location = destination;
		destination.contents.add(thing);
		return thing;
	}
}

