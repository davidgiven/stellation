package model;

import interfaces.IDatastore;
import utils.Fault;
import utils.Oid;
import utils.Injectomatic.inject;

class AbstractProperty {
    public var name: String;

    public function new(name: String) {
        this.name = name;
    }

    public function createProperty(datastore: IDatastore): Void {
        throw Fault.UNIMPLEMENTED;
    }
}

class IntProperty extends AbstractProperty {
    public function new(name: String) {
        super(name);
    }

    public function get(thing: SThing): Int {
        return thing.datastore.getIntProperty(thing.oid, name);
    }

    public function set(thing: SThing, value: Int): Int {
        thing.datastore.setIntProperty(thing.oid, name, value);
        return value;
    }

    public override function createProperty(datastore: IDatastore): Void {
        datastore.createProperty(name, "INTEGER", false);
    }
}

class FloatProperty extends AbstractProperty {
    public function new(name: String) {
        super(name);
    }

    public function get(thing: SThing): Float {
        return thing.datastore.getFloatProperty(thing.oid, name);
    }

    public function set(thing: SThing, value: Float): Float {
        thing.datastore.setFloatProperty(thing.oid, name, value);
        return value;
    }

    public override function createProperty(datastore: IDatastore): Void {
        datastore.createProperty(name, "REAL", false);
    }
}

class StringProperty extends AbstractProperty {
    public function new(name: String) {
        super(name);
    }

    public function get(thing: SThing): String {
        return thing.datastore.getStringProperty(thing.oid, name);
    }

    public function set(thing: SThing, value: String): String {
        thing.datastore.setStringProperty(thing.oid, name, value);
        return value;
    }

    public override function createProperty(datastore: IDatastore): Void {
        datastore.createProperty(name, "TEXT", false);
    }
}

class ObjectProperty<T: SThing> extends AbstractProperty {
    private var type: Class<T>;

    public function new(name: String, type: Class<T>) {
        super(name);
        this.type = type;
    }

    public function get(thing: SThing): Null<T> {
        var oid = thing.datastore.getOidProperty(thing.oid, name);
        return thing.objectLoader.loadObject(oid, type);
    }

    public function set(thing: SThing, value: Null<T>): Null<T> {
        if (value == null) {
            thing.datastore.setOidProperty(thing.oid, name, null);
        } else {
            thing.datastore.setOidProperty(thing.oid, name, value.oid);
        }
        return value;
    }

    public override function createProperty(datastore: IDatastore): Void {
        datastore.createProperty(name, "INTEGER REFERENCES objects(oid) ON DELETE CASCADE", false);
    }
}

class ObjectSet<T: SThing> {
    private var objectLoader: ObjectLoader;
    private var underlying: OidSet;
    private var klass: Class<T>;

    public function new(objectLoader: ObjectLoader, underlying: OidSet, klass: Class<T>) {
        this.objectLoader = objectLoader;
        this.underlying = underlying;
        this.klass = klass;
    }

    public function add(item: T): ObjectSet<T> {
        underlying.add(item.oid);
        return this;
    }

    public function remove(item: T): ObjectSet<T> {
        underlying.remove(item.oid);
        return this;
    }

    public function clear(): ObjectSet<T> {
        underlying.clear();
        return this;
    }

    public function exists(item: T): Bool {
        return underlying.exists(item.oid);
    }

    public function getAll(): Iterable<T> {
        return {
            iterator: () -> {
                var ui = underlying.getAll().iterator();
                return {
                    next: () -> load(ui.next()),
                    hasNext: () -> ui.hasNext(),
                };
            }
        };
    }

	public function getOne(): Null<T> {
        return load(underlying.getOne());
    }

    private function load(oid: Null<Oid>): Null<T> {
        if (oid == null) {
            return null;
        } else {
            return objectLoader.loadObject(oid, klass);
        }
    }
}

class SetProperty<T: SThing> extends AbstractProperty {
    private var klass: Class<T>;

    public function new(name: String, klass: Class<T>) {
        super(name);
        this.klass = klass;
    }

    public function get(thing: SThing): ObjectSet<T> {
        var oidSet = thing.datastore.getSetProperty(thing.oid, name);
        return new ObjectSet(thing.objectLoader, oidSet, klass);
    }

    public function set(thing: SThing, value: ObjectSet<T>): ObjectSet<T> {
        throw Fault.UNIMPLEMENTED;
    }

    public override function createProperty(datastore: IDatastore): Void {
        datastore.createProperty(name, "INTEGER REFERENCES objects(oid) ON DELETE CASCADE", true);
    }
}

class Properties {
    public static var KIND = new StringProperty("kind");
    public static var BRIGHTNESS = new FloatProperty("brightness");
    public static var CONTENTS = new SetProperty("contents", SThing);
    public static var GALAXY = new ObjectProperty("galaxy", SGalaxy);
    public static var LOCATION = new ObjectProperty("location", SThing);
    public static var NAME = new StringProperty("name");
    public static var OWNER = new ObjectProperty("owner", SThing);
}

