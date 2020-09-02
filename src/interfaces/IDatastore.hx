package interfaces;

import utils.Oid;
import tink.CoreApi;

interface OidSet {
    public function add(item: Oid): OidSet;
    public function remove(item: Oid): OidSet;
    public function clear(): OidSet;
    public function exists(item: Oid): Bool;
    public function getAll(): Iterable<Oid>;
	public function getOne(): Null<Oid>;
}

interface IDatastore {
    public function initialiseDatabase(): Void;

    public function createProperty(name: String, sqlType: String, isAggregate: Bool): Void;

    public function createObject(): Oid;
    public function createSpecificObject(oid: Oid): Void;
    public function destroyObject(oid: Oid): Void;
    public function getAllObjects(): Iterable<Oid>;
    public function doesObjectExist(oid: Oid): Bool;

    public function hasProperty(oid: Oid, name: String): Bool;

    public function setOidProperty(oid: Oid, name: String, value: Null<Oid>): Void;
    public function getOidProperty(oid: Oid, name: String): Null<Oid>;

    public function setIntProperty(oid: Oid, name: String, value: Int): Void;
    public function getIntProperty(oid: Oid, name: String): Int;

    public function setFloatProperty(oid: Oid, name: String, value: Float): Void;
    public function getFloatProperty(oid: Oid, name: String): Float;

    public function setStringProperty(oid: Oid, name: String, value: String): Void;
    public function getStringProperty(oid: Oid, name: String): String;

    public function getSetProperty(oid: Oid, name: String): OidSet;

    public function createSyncSession(): Int;
    public function getPropertiesChangedSince(oids: Iterable<Oid>, session: Int): Iterable<Pair<Oid, String>>;
    public function propertySeenBy(oid: Oid, name: String, session: Int): Void;
	public function withTransaction(callback: () -> Void): Void;

    public function getHierarchy(root: Oid, containment: String): Map<Oid, Noise>;
}

