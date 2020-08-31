package model;

import interfaces.IDatastore;
import utils.Injectomatic.inject;
import utils.Oid;
import utils.Fault;
import utils.FaultDomain;
using utils.ArrayTools;

class ObjectLoader {
    private static final CLASSES: Map<String, Class<SThing>> = [
        "SThing" => SThing,
        "SUniverse" => SUniverse,
    ];

	public var datastore = inject(IDatastore);

	public function new() {
	}

    public function loadRawObject<T: SThing>(oid: Oid, klass: Class<T>): T {
        if (!datastore.doesObjectExist(oid)) {
            throw objectNotVisibleException(oid);
        }

        var kind = datastore.getStringProperty(oid, "kind");
        var dbklass = CLASSES[kind];
        if (dbklass == null) {
            throw databaseBadKindException(oid, kind);
        }

        var instance = Type.createInstance(dbklass, []);
        if (Std.isOfType(instance, klass)) {
            instance.oid = oid;
            instance.kind = dbklass;
            return Std.downcast(instance, klass);
        }

        throw databaseTypeMismatchException(oid, kind, getSimpleName(klass));
    }
    
    public function loadObject<T: SThing>(oid: Oid, klass: Class<T>): T {
        return loadRawObject(oid, klass);
    }

    public function createObject<T: SThing>(klass: Class<T>): T {
        var oid = datastore.createObject();
        datastore.setStringProperty(oid, "kind", getSimpleName(klass));
        return loadObject(oid, klass);
    }

    private static function objectNotVisibleException(oid: Oid): Fault {
        return new Fault(INVALID_ARGUMENT).withDetail('object $oid does not exist or is not visible');
    }

    private static function databaseTypeMismatchException(oid: Oid, kind: String, desired: String): Fault {
        return new Fault(INVALID_ARGUMENT).withDetail('expected $oid to be a $desired, but it was a $kind');
    }

    private static function databaseBadKindException(oid: Oid, kind: String): Fault {
        return new Fault(INTERNAL).withDetail('database $oid has kind $kind which I don\'t understand');
    }

    private static function getSimpleName<T: SThing>(klass: Class<T>): String {
        return Type.getClassName(klass).split(".").last();
    }
}

