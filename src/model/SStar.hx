package model;

import model.Properties;
import model.Properties.POSITION;
import model.Properties.BRIGHTNESS;
import model.Properties.NAME;
import interfaces.ILogger.Logger.log;
using utils.ArrayTools;

@:tink
class SStar extends SThing {
	private static final GLOBAL_PROPERTIES = [POSITION, BRIGHTNESS, NAME].toMap();

	@:sproperty public var position: XY;
	@:sproperty public var brightness: Float;
	@:sproperty public var asteroids: MC;

	public override function hasGlobalVisibility(property: AbstractProperty) {
		if (GLOBAL_PROPERTIES.exists(property))
			return true;
		return super.hasGlobalVisibility(property);
	}
}

