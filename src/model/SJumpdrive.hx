package model;

import model.Properties;

class SJumpdrive extends SModule {
	public override function getConsumption(): AMO {
		return { a: 5.0, m: 2.0, o: 0.0 };
	}
}

