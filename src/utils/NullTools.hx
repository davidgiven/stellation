package utils;

class NullTools {
	public static inline function or<T>(n: Null<T>, d: T): T {
		if (n == null) {
			return d;
		} else {
			return n;
		}
	}

	public static inline function then<T, P>(n: Null<T>, cb: (T) -> P): Null<P> {
		if (n == null) {
			return null;
		} else {
			return cb(n);
		}
	}
}

