package model;

import model.Properties;

class SDrones extends SModule {
	public override function getConsumption(): AMO {
		return { a: 5.0, m: 0.0, o: 0.0 };
	}
}



