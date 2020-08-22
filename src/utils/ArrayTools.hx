package utils;

class ArrayExtender {
	@:generic
	public static function getOrElse<T>(array: Array<T>, index: Int, def: T): T {
		if ((index < 0) || (index >= array.length)) {
			return def;
		}
		return array[index];
	}
}

