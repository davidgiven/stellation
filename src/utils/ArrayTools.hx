package utils;

import tink.CoreApi;

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

    @:generic
    public static function toArray<T>(iterator: Iterator<T>): Array<T> {
        var result: Array<T> = [];
        for (value in iterator) {
            result.push(value);
        }
        return result;
    }

    @:generic
    public static function map<T1, T2>(iterator: Iterator<T1>, callback: (T1) -> T2): Iterator<T2> {
        return {
            hasNext: iterator.hasNext,
            next: () -> callback(iterator.next())
        }
    }

	@:generic
	public static function zip<T1, T2>(i1: Iterator<T1>, i2: Iterator<T2>): Iterator<Pair<T1, T2>> {
		return {
			hasNext: () -> i1.hasNext() || i2.hasNext(),
			next: () -> new Pair(i1.next(), i2.next())
		}
	}
}

