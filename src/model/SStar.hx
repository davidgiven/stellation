package model;

import model.Properties;
import model.Properties.X;
import model.Properties.Y;
import model.Properties.BRIGHTNESS;
import model.Properties.NAME;
import interfaces.ILogger.Logger.log;
using utils.ArrayTools;

@:tink
class SStar extends SThing {
	private static final GLOBAL_PROPERTIES = [X, Y, BRIGHTNESS, NAME].toMap();

	@:sproperty public var x: Float;
	@:sproperty public var y: Float;
	@:sproperty public var brightness: Float;
	@:sproperty public var asteroidsm: Int;
	@:sproperty public var asteroidsc: Int;

	public override function hasGlobalVisibility(property: AbstractProperty) {
		if (GLOBAL_PROPERTIES.exists(property))
			return true;
		return super.hasGlobalVisibility(property);
	}
}

