package model;

import interfaces.IDatastore;
import utils.Fault;
import utils.Oid;
import utils.Injectomatic.inject;

class AbstractProperty {
    var name: String;

    public function new(name: String) {
        this.name = name;
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
}

class OidProperty<T: SThing> extends AbstractProperty {
    private var type: Class<T>;

    public function new(name: String, type: Class<T>) {
        super(name);
        this.type = type;
    }

    public function get(thing: SThing): Null<T> {
        var oid = thing.datastore.getOidProperty(thing.oid, name);
        return thing.objectLoader.loadObject(oid, type);
    }

    public function set(thing: SThing, value: T): T {
        thing.datastore.setOidProperty(thing.oid, name, value.oid);
        return value;
    }
}

class SetProperty extends AbstractProperty {
    public function new(name: String) {
        super(name);
    }

    public function get(thing: SThing): OidSet return null;
    public function set(thing: SThing, value: OidSet): OidSet throw Fault.UNIMPLEMENTED;
}

class Properties {
    public static var CONTENTS = new SetProperty("contents");
    public static var LOCATION = new OidProperty("location", SThing);
    public static var NAME = new StringProperty("name");
    public static var OWNER = new OidProperty("owner", SThing);
    public static var KIND = new StringProperty("kind");
}

