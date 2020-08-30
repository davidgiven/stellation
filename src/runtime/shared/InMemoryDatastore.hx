package runtime.shared;

import interfaces.IDatastore;
import utils.Oid;
import tink.CoreApi;
import utils.Fault.UNIMPLEMENTED;

class InMemorySetProperty implements SetProperty {
	private var data: Map<Oid, Noise> = [];

	public function new() {}

	public function add(oid: Oid): SetProperty {
		data[oid] = Noise;
		return this;
	}

	public function clear(): SetProperty {
		data.clear();
		return this;
	}

	public function exists(oid: Oid): Bool {
		return data.exists(oid);
	}

	public function remove(oid: Oid): SetProperty {
		data.remove(oid);
		return this;
	}

	public function getAll(): Iterable<Oid> {
		return {
			iterator: () -> data.keys()
		};
	}

	public function getOne(): Null<Oid> {
		return data.keys().next();
	}
}

class InMemoryDatastore implements IDatastore {
    private var maxOid: Oid;
    private var objects: Map<Oid, Noise>;
    private var values: Map<String, Dynamic>;
    private var properties: Map<String, Noise>;

	public function new() {}

	public function initialiseDatabase(): Void {
        maxOid = 1;
        objects = [];
        values = [];
        properties = [];
	}

    public function createProperty(name: String, sqlType: String, isAggregate: Bool): Void {
		properties[name] = Noise;
    }

	public function createObject(): Oid {
		var newOid = maxOid++;
		objects[newOid] = Noise;
		return newOid;
	}

    public function createSpecificObject(oid: Oid): Void {
        if (oid > maxOid) {
            maxOid = oid + 1;
        }
        objects[oid] = Noise;
    }

    public function destroyObject(oid: Oid): Void {
        objects.remove(oid);
    }

	public function getAllObjects(): Iterable<Oid> {
		return {
			iterator: () -> objects.keys()
		};
	}

	public function doesObjectExist(oid: Oid): Bool {
		return objects.exists(oid);
	}
	
	public function hasProperty(oid: Oid, name: String): Bool {
		return values.exists(wrap(oid, name));
	}


	public function getOidProperty(oid: Oid, name: String): Null<Oid> {
		var value = values[wrap(oid, name)];
		if (value == null) {
			return null;
		}
		return cast(value, Oid);
	}
	
	public function setOidProperty(oid: Oid, name: String, value: Null<Oid>): Void {
		if (value == null) {
			values[wrap(oid, name)] = null;
		} else {
			values[wrap(oid, name)] = value;
		}
	}

	public function getIntProperty(oid: Oid, name: String): Int {
		return cast(values[wrap(oid, name)], Int);
	}

	public function setIntProperty(oid: Oid, name: String, value: Int): Void {
		values[wrap(oid, name)] = value;
	}

	public function getFloatProperty(oid: Oid, name: String): Float {
		return cast(values[wrap(oid, name)], Float);
	}

	public function setFloatProperty(oid: Oid, name: String, value: Float): Void {
		values[wrap(oid, name)] = value;
	}

	public function getStringProperty(oid: Oid, name: String): String {
		return cast(values[wrap(oid, name)], String);
	}

	public function setStringProperty(oid: Oid, name: String, value: String): Void {
		values[wrap(oid, name)] = value;
	}

	public function getSetProperty(oid: Oid, name: String): SetProperty {
		var value = values[wrap(oid, name)];
		if (value == null) {
			value = new InMemorySetProperty();
			values[wrap(oid, name)] = value;
		}
		return cast(value, InMemorySetProperty);
	}

	public function createSyncSession(): Int throw UNIMPLEMENTED;
	public function getPropertiesChangedSince(oids: Iterable<Oid>, session: Int): Iterable<Pair<Oid, String>> throw UNIMPLEMENTED;
	public function propertySeenBy(oid: Oid, name: String, session: Int): Void throw UNIMPLEMENTED;

	public function getHierarchy(root: Oid, containment: String): Map<Oid, Noise> {
        var set: Map<Oid, Noise> = [root => Noise];
        for (child in getSetProperty(root, containment).getAll()) {
			for (key in getHierarchy(child, containment).keys()) {
				set[key] = true;
			}
        }
        return set;
	}

	private static inline function wrap(oid: Oid, name: String): String {
		return '${oid}:${name}';
	}
}

