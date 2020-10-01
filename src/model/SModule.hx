package model;

import model.Properties;

class SModule extends SPhysicalThing {
	public function getConsumption(): AMO {
		return { a: 0.0, m: 0.0, o: 0.0 };
	}

	public function getMass(): Float {
		return 1.0;
	}
}

