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

	public static function toMap(oids: Iterable<Oid>) {
		var m: Map<Oid, Noise> = [];
		for (item in oids) {
			m[item] = true;
		}
		return m;
	}
}

