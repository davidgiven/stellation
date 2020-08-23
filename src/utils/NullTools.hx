package utils;

class NullTools {
	public static inline function or<T>(n: Null<T>, d: T): T {
		if (n == null) {
			return d;
		} else {
			return n;
		}
	}
}

