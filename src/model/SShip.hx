package model;

import model.Properties.OWNER;
import model.Properties.MASS;
import model.Properties;
import model.SModule;
using utils.ArrayTools;

class SShip extends SPhysicalThing {
	private static final GLOBAL_PROPERTIES = [OWNER, MASS].toMap();

	@:sproperty public var consumption: AMO;
	@:sproperty public var fuel: AMO;

	public override function hasGlobalVisibility(property: AbstractProperty) {
		if (GLOBAL_PROPERTIES.exists(property))
			return true;
		return super.hasGlobalVisibility(property);
	}

	public function update() {
		var m = 0.0;
		var c = { a: 0.0, m: 0.0, o: 0.0 };
		for (thing in contents.getAll()) {
			var module = cast(thing, SModule);
			var mc = module.getConsumption();
			c.a += mc.a;
			c.m += mc.m;
			c.o += mc.o;

			m += module.getMass();
		}
		consumption = c;
		mass = m;
	}
}

