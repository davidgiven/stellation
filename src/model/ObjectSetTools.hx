package model;

import model.Properties;
import tink.CoreApi;

class ObjectSetTools {
	@:generic
	public static function addAll<T: SThing>(set: ObjectSet<T>, items: Iterable<T>): ObjectSet<T> {
		for (item in items) {
			set.add(item);
		}
		return set;
	}

	@:generic
	public static function replaceAll<T: SThing>(set: ObjectSet<T>, items: Iterable<T>): ObjectSet<T> {
		set.clear();
		for (item in items) {
			set.add(item);
		}
		return set;
	}

	@:generic
	public static inline function setScope<T: AbstractProperty>(property: T, scope: Scope): T {
		property.scope = scope;
		return property;
	}
}

