package model;

import model.Properties;

class SModule extends SPhysicalThing {
	@:sproperty public var active: Int;
	@:sproperty public var moduleIndex: Int;

	public override function init(): Void {
		active = 0;
		super.init();
	}

	public function getConsumption(): AMO {
		return { a: 0.0, m: 0.0, o: 0.0 };
	}

	public function getMass(): Float {
		return 1.0;
	}
}

