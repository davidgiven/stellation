package model;

import model.Properties;
import model.Properties.HULLDATA;
import utils.Fault;
import utils.FaultDomain;
using model.ThingTools;

class SHull extends SThing {
	private static final MAX_SIZE = 10;

	@:sproperty public var hullData: Hull;

	public override function hasGlobalVisibility(property: AbstractProperty) {
		if (property == HULLDATA)
			return true;
		return super.hasGlobalVisibility(property);
	}

	public function setFrameData(hull: Hull): Void {
		if (hull == null) {
			throw invalidData();
		}

		if ((hull.width < 0) || (hull.width >= MAX_SIZE)
				|| (hull.height < 0) || (hull.height >= MAX_SIZE)) {
			throw new Fault(INVALID_ARGUMENT).withDetail("you can't make hulls that big");
		}

		if (hull.modules.length != (hull.width * hull.height)) {
			throw new Fault(INVALID_ARGUMENT).withDetail("invalid module information");
		}

		var classes = objectLoader.getAllClasses();
		for (module in hull.modules) {
			if (module == null) {
				continue;
			}

			var klass = classes[module];
			if (klass == null) {
				throw invalidModuleType(module);
			}

			var superclass = Type.getSuperClass(klass);
			while ((superclass != SThing) && (superclass != SModule)) {
				superclass = Type.getSuperClass(superclass);
			}

			if (superclass == SThing) {
				throw invalidModuleType(module);
			}
		}

		hullData = hull;
	}

	public function createShip(): SShip {
		var ship = objectLoader.createObject(SShip);
		ship.owner = owner;
		ship.name = '${name} #${ship.oid}';
		ship.hullData = hullData;
		owner.ships.add(ship);

		var classes = objectLoader.getAllClasses();
		for (index => module in hullData.modules) {
			if (module == null) {
				continue;
			}

			var klass = classes[module];
			var object = cast(objectLoader.createObject(klass), SModule);
			object.owner = owner;
			object.moduleIndex = index;
			object.moveTo(ship);
		}

		return ship;
	}

	private static function invalidData(): Fault {
		return new Fault(INVALID_ARGUMENT).withDetail('bad module data');
	}

	private static function invalidModuleType(name: String): Fault {
		return new Fault(INVALID_ARGUMENT).withDetail('$name is not a valid module type');
	}
}


