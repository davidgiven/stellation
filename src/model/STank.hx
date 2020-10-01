package model;

import model.Properties;

class STank extends SModule {
	private static final SCALE = 300.0;
	private static final BASE_MASS = 5000.0;

	@:sproperty public var fuel: AMO;
	
	public override function init(): Void {
		fuel = { a: 0.0, m: 0.0, o: 0.0 };
	}

	public override function getMass(): Float {
		return 5000 + fuel.a/SCALE + fuel.m/SCALE + fuel.o/SCALE;
	}

	public override function getConsumption(): AMO {
		return { a: 5.0, m: 0.0, o: 0.0 };
	}
}


