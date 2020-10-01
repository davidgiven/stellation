package model;

import model.Properties.OWNER;
import model.Properties.MASS;
import model.Properties;
using utils.ArrayTools;

class SShip extends SPhysicalThing {
	private static final GLOBAL_PROPERTIES = [OWNER, MASS].toMap();

	public override function hasGlobalVisibility(property: AbstractProperty) {
		if (GLOBAL_PROPERTIES.exists(property))
			return true;
		return super.hasGlobalVisibility(property);
	}
}

