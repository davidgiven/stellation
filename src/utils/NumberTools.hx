package utils;
using Math;

class NumberTools {
	public static function roundBy(value: Float, factor: Float): Float {
		return (value * factor).fround() / factor;
	}
}

