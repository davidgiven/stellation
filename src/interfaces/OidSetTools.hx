package interfaces;

import interfaces.IDatastore;
import utils.Oid;
import tink.CoreApi;

class OidSetTools {
    public static inline function addAll(set: OidSet, oids: Iterable<Oid>) {
        for (oid in oids) {
            set.add(oid);
        }
    }

    public static inline function replaceAll(set: OidSet, oids: Iterable<Oid>) {
        set.clear();
        addAll(set, oids);
    }
}

