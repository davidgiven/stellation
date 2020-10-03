package model;

import model.Properties;
import model.Properties.HULLDATA;

class SHull extends SThing {
	@:sproperty public var hullData: Hull;

	public override function hasGlobalVisibility(property: AbstractProperty) {
		if (property == HULLDATA)
			return true;
		return super.hasGlobalVisibility(property);
	}

	public function setFrameData(hullData: Hull): Void {

	}
}


