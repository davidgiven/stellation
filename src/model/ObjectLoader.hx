package model;

import interfaces.IDatastore;
import utils.Injectomatic.inject;
import utils.Oid;
import utils.Fault;
import utils.FaultDomain;
import model.Properties;
using utils.ArrayTools;
using Lambda;
using Type;

class ObjectLoader {
    private static final KIND = Properties.KIND.name;

    private static final CLASSES = registerClasses([
        SFactory,
        SGalaxy,
        SModule,
        SStar,
        SThing,
        SUniverse,
    ]);

	public var datastore = inject(IDatastore);

    public var objectCache: Map<Oid, SThing> = [];

	public function new() {
	}

    public function initialiseProperties(): Void {
        for (field in Properties.getClassFields()) {
            var o = Reflect.field(Properties, field);
            if (Std.is(o, AbstractProperty)) {
                cast(o, AbstractProperty).createProperty(datastore);
            }
        }
    }

    public function loadRawObject<T: SThing>(oid: Oid, klass: Class<T>): T {
        if (!datastore.doesObjectExist(oid)) {
            throw objectNotVisibleException(oid);
        }

        var kind = datastore.getStringProperty(oid, KIND);
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
    
    public function loadObject<T: SThing>(oid: Null<Oid>, klass: Class<T>): Null<T> {
        if (oid == null) {
            return null;
        }
        var instance = objectCache[oid];
        if (instance == null) {
            instance = loadRawObject(oid, klass);
            objectCache[oid] = instance;
        }
        if (Std.isOfType(instance, klass)) {
            return Std.downcast(instance, klass);
        }
        throw databaseTypeMismatchException(oid, getSimpleName(instance.kind), getSimpleName(klass));
    }

    public function createObject<T: SThing>(klass: Class<T>): T {
        var oid = datastore.createObject();
        datastore.setStringProperty(oid, KIND, getSimpleName(klass));
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

    private static function registerClasses(klasses: Iterable<Class<SThing>>): Map<String, Class<SThing>> {
        return [for (klass in klasses) getSimpleName(klass) => klass];
    }

    public function createUniverse(): SUniverse {
        if (datastore.doesObjectExist(1)) {
            throw new Fault(INTERNAL).withDetail("cowardly refusing to destroy existing universe");
        }

        datastore.createSpecificObject(1);
        datastore.setStringProperty(1, KIND, "SUniverse");
        return loadObject(1, SUniverse);
    }
}

