package utils;

class ArrayTools {
	@:generic
	public static function getOrElse<T>(array: Array<T>, index: Int, def: T): T {
		if ((index < 0) || (index >= array.length)) {
			return def;
		}
		return array[index];
	}

    @:generic
    public static inline function last<T>(a: Array<T>): T {
        return a[a.length - 1];
    }
}

