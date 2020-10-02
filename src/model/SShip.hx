package model;

import model.Properties.OWNER;
import model.Properties.MASS;
import model.Properties;
import model.SModule;
using utils.ArrayTools;
using Math;

class SShip extends SPhysicalThing {
	private static final GLOBAL_PROPERTIES = [OWNER, MASS].toMap();

	@:sproperty public var consumption: AMO;
	@:sproperty public var mass: Float;

	public override function init() {
		update();
		super.init();

		pokeMaintenanceTimer();
	}

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

	@:keep
	public function consumeMaintenanceCosts(duration: Float): Void {
		var c = consumption;
		var ca = c.a * duration;
		var cm = c.m * duration;
		var co = c.o * duration;
		for (thing in contents.getAll()) {
			if (Std.is(thing, STank)) {
				var tank: STank = cast thing;
				var f = tank.fuel;
				var delta_a = f.a.min(ca);
				var delta_m = f.m.min(cm);
				var delta_o = f.o.min(co);
				f.a -= delta_a;
				f.m -= delta_m;
				f.o -= delta_o;
				tank.fuel = f;

				ca -= delta_a;
				cm -= delta_m;
				co -= delta_o;
			}
		}

		pokeMaintenanceTimer();
	}

	public function pokeMaintenanceTimer() {
		timers.setTimer(oid, "consumeMaintenanceCosts", clock.getTime() + 10.0);
	}
}

