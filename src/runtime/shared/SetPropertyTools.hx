package runtime.shared;

import interfaces.IDatastore;
import utils.Oid;
import tink.CoreApi;

class SetPropertyTools {
    public static inline function addAll(set: OidSet, oids: Iterable<Oid>) {
        for (oid in oids) {
            set.add(oid);
        }
    }

    public static inline function replaceAll(set: OidSet, oids: Iterable<Oid>) {
        set.clear();
        addAll(set, oids);
    }

    @:generic
	public static function toMap<T>(oids: Iterable<T>) {
		var m: Map<T, Noise> = [];
		for (item in oids) {
			m[item] = Noise;
		}
		return m;
	}
}

