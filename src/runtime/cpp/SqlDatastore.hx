package runtime.cpp;

import haxe.Serializer;
import haxe.Unserializer;
import interfaces.IDatastore;
import model.Properties;
import runtime.cpp.Sqlite;
import runtime.shared.Time;
import tink.CoreApi;
import utils.Fault;
import utils.Injectomatic.inject;
import utils.Oid;
using utils.ArrayTools;

class SqlOidSet implements OidSet {
    var datastore: SqlDatastore;
    var db: SqliteDatabase;
    var oid: Oid;
    var name: String;

    public function new(datastore: SqlDatastore, db: SqliteDatabase, oid: Oid, name: String) {
        this.datastore = datastore;
        this.db = db;
        this.oid = oid;
        this.name = name;
    }

    public function add(item: Oid): OidSet {
        db.sqlStatement('INSERT OR IGNORE INTO prop_$name (oid, value) VALUES (?, ?)')
                .bindInt(1, oid)
                .bindOid(2, item)
                .executeStatement();
        datastore.propertyChanged(oid, name);
        return this;
    }

    public function remove(item: Oid): OidSet {
        db.sqlStatement('DELETE FROM prop_$name WHERE oid = ? AND value = ?')
                .bindInt(1, oid)
                .bindOid(2, item)
                .executeStatement();
        datastore.propertyChanged(oid, name);
        return this;
    }

    public function clear(): OidSet {
        db.sqlStatement('DELETE FROM prop_$name WHERE oid = ?')
                .bindInt(1, oid)
                .executeStatement();
        datastore.propertyChanged(oid, name);
        return this;
    }

    public function exists(item: Oid): Bool {
        return db.sqlStatement(
                'SELECT COUNT(*) AS count FROM prop_$name WHERE oid = ? AND value = ? LIMIT 1')
                .bindInt(1, oid)
                .bindOid(2, item)
                .executeSimpleQuery()
                .get("count")
                .toInt() != 0;
    }

    public function getAll(): Iterable<Oid> {
        return db.sqlStatement('SELECT value FROM prop_$name WHERE oid = ?')
                .bindInt(1, oid)
                .executeQuery()
                .map(it -> it.get("value").toOid())
                .toArray();
    }

    public function getOne(): Null<Oid> {
        var results = db.sqlStatement('SELECT value FROM prop_$name WHERE oid = ?')
                .bindInt(1, oid)
                .executeSimpleQuery();
        return (results == null) ? null : results.get("value").toOid();
    }
}

class SqlDatastore implements IDatastore {
	var time = inject(Time);
	var db = inject(SqliteDatabase);

    public function new() {
        db.executeSql('PRAGMA encoding = "UTF-8"');
        db.executeSql('PRAGMA synchronous = OFF');
        db.executeSql('PRAGMA foreign_keys = ON');
        db.executeSql('PRAGMA temp_store = MEMORY');
    }

    public function close(): Void {
        db.close();
        db = null;
    }

    public function initialiseDatabase(): Void {
        db.executeSql(
                '
                CREATE TABLE IF NOT EXISTS objects (
                    oid INTEGER PRIMARY KEY AUTOINCREMENT
                )
            ');

        db.executeSql(
                '
                CREATE TABLE IF NOT EXISTS sessions (
                    session INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                    lastused REAL NOT NULL
                )
            ');

        db.executeSql(
                '
                CREATE TABLE IF NOT EXISTS seen_by (
                    oid INTEGER NOT NULL REFERENCES objects(oid) ON DELETE CASCADE,
                    name TEXT NOT NULL,
                    session INTEGER NOT NULL REFERENCES sessions(session) ON DELETE CASCADE,
                    PRIMARY KEY (oid, name, session)
                )
            ');
        db.executeSql(
                '
                CREATE INDEX IF NOT EXISTS seen_by_by_session ON seen_by (session)
            ');

        db.executeSql(
                '
                CREATE TABLE IF NOT EXISTS properties (
                    oid INTEGER NOT NULL REFERENCES objects(oid) ON DELETE CASCADE,
                    name TEXT NOT NULL,
                    PRIMARY KEY (oid, name)
                )
            ');
        db.executeSql(
                '
                CREATE INDEX IF NOT EXISTS properties_by_oid ON properties (oid)
            ');
    }

    public function createProperty(name: String, sqlType: String, isAggregate: Bool): Void {
        if (!isAggregate) {
            db.executeSql(
                    '
                 CREATE TABLE IF NOT EXISTS prop_$name (
                    oid INTEGER PRIMARY KEY NOT NULL REFERENCES objects(oid) ON DELETE CASCADE,
                    value $sqlType)
            ');
        } else {
            db.executeSql(
                    '
                 CREATE TABLE IF NOT EXISTS prop_$name (
                    oid INTEGER NOT NULL REFERENCES objects(oid) ON DELETE CASCADE,
                    value $sqlType)
            ');
        }
    }

    public function createObject(): Oid {
        db.executeSql('INSERT INTO objects (oid) VALUES(NULL)');
        return db.sqlStatement('SELECT last_insert_rowid() AS oid')
                .executeSimpleQuery()["oid"].toOid();
    }

    public function createSpecificObject(oid: Oid): Void {
        db.sqlStatement('INSERT INTO objects (oid) VALUES(?)')
                .bindOid(1, oid)
                .executeStatement();
    }

    public function destroyObject(oid: Oid) {
        db.sqlStatement('DELETE FROM objects WHERE oid = ?')
                .bindInt(1, oid)
                .executeStatement();
    }

    public function getAllObjects(): Iterable<Oid> {
        return [
            for (row in db.sqlStatement('SELECT oid FROM objects').executeQuery())
                row["oid"].toOid()
        ];
    }

    public function doesObjectExist(oid: Oid): Bool {
        return db.sqlStatement('SELECT COUNT(*) AS count FROM objects WHERE oid = ? LIMIT 1')
                .bindOid(1, oid)
                .executeSimpleQuery()
                .get("count")
                .toInt() != 0;
    }

    public function hasProperty(oid: Oid, name: String): Bool {
        return db.sqlStatement('SELECT EXISTS(SELECT * FROM prop_$name WHERE oid = ?) as e')
                .bindOid(1, oid)
                .executeSimpleQuery()
                .get("e")
                .toInt() == 1;
    }

    private function setStatement(oid: Oid, name: String): SqliteStatement {
        return db.sqlStatement('INSERT OR REPLACE INTO prop_$name (oid, value) VALUES (?, ?)')
                .bindOid(1, oid);
    }

    private function getStatement(oid: Oid, name: String): SqliteStatement {
        return db.sqlStatement('SELECT value FROM prop_$name WHERE oid = ?')
                .bindOid(1, oid);
    }

    public function setOidProperty(oid: Oid, name: String, value: Null<Oid>): Void {
        setStatement(oid, name).bindOid(2, value)
                .executeStatement();
		propertyChanged(oid, name);
    }

    public function getOidProperty(oid: Oid, name: String): Null<Oid> {
        var results = getStatement(oid, name).executeSimpleQuery();
        return (results == null) ? null : results.get("value").toOid();
    }

    public function setIntProperty(oid: Oid, name: String, value: Int): Void {
        setStatement(oid, name).bindInt(2, value)
                .executeStatement();
		propertyChanged(oid, name);
    }

    public function getIntProperty(oid: Oid, name: String): Int {
        return getStatement(oid, name)
                .executeSimpleQuery()
                .get("value")
                .toInt();
    }

    public function setFloatProperty(oid: Oid, name: String, value: Float): Void {
        setStatement(oid, name).bindFloat(2, value)
                .executeStatement();
		propertyChanged(oid, name);
    }

    public function getFloatProperty(oid: Oid, name: String): Float {
        return getStatement(oid, name)
                .executeSimpleQuery()
                .get("value")
                .toFloat();
    }

    public function setStringProperty(oid: Oid, name: String, value: String): Void {
        setStatement(oid, name).bindString(2, value)
                .executeStatement();
		propertyChanged(oid, name);
    }

    public function getStringProperty(oid: Oid, name: String): String {
        var results = getStatement(oid, name).executeSimpleQuery();
        return (results == null) ? null : results.get("value").toString();
    }

    public function setStructProperty<T>(oid: Oid, name: String, value: T): Void {
		var s = new Serializer();
		s.useCache = true;
		s.serialize(value);
		setStringProperty(oid, name, s.toString());
	}

    public function getStructProperty<T>(oid: Oid, name: String): T {
		var bytes = getStringProperty(oid, name);
		var u = new Unserializer(bytes);
		u.setResolver(null);
		return u.unserialize();
	}

    public function getSetProperty(oid: Oid, name: String): OidSet {
        return new SqlOidSet(this, db, oid, name);
    }

    public function propertyChanged(oid: Oid, name: String): Void {
        db.sqlStatement('INSERT OR IGNORE INTO properties (oid, name) VALUES (?, ?)')
                .bindOid(1, oid)
                .bindString(2, name)
                .executeStatement();
        db.sqlStatement('DELETE FROM seen_by WHERE oid = ? AND name = ?')
                .bindOid(1, oid)
                .bindString(2, name)
                .executeStatement();
    }

    public function createSyncSession(): Int {
		db.sqlStatement('INSERT INTO sessions (lastused) VALUES (?)')
                .bindFloat(1, time.realtime())
                .executeStatement();
        return db.sqlStatement('SELECT last_insert_rowid() AS session')
                .executeSimpleQuery()
				.get("session")
				.toInt();
	}

    public function getPropertiesChangedSince(oids: Iterable<Oid>, session: Int): Iterable<Pair<Oid, String>> {
		db.executeSql('DROP TABLE IF EXISTS _temp_oids');
		db.executeSql(
			'CREATE TEMPORARY TABLE _temp_oids (
				oid INTEGER PRIMARY KEY
			)');
		for (oid in oids) {
			db.sqlStatement('INSERT INTO _temp_oids (oid) VALUES (?)')
				.bindOid(1, oid)
				.executeStatement();
		}

		var results: Array<Pair<Oid, String>> = [];
        for (row in db.sqlStatement('
                    SELECT
                        oid, name
                    FROM
                        properties
                    WHERE
                        EXISTS (SELECT * FROM _temp_oids WHERE _temp_oids.oid = properties.oid)
                        AND NOT EXISTS (
                            SELECT
                                *
                            FROM
                                seen_by
                            WHERE
                                seen_by.oid = properties.oid
                                AND seen_by.name = properties.name
                                AND seen_by.session = ?
                        )
                ')
                .bindInt(1, session)
                .executeQuery()) {
			var oid = row.get("oid").toOid();
			var name = row.get("name").toString();
			results.push(new Pair(oid, name));
		}
		return results;
	}

    public function propertySeenBy(oid: Oid, name: String, session: Int): Void {
        db.sqlStatement('INSERT INTO seen_by (oid, name, session) VALUES (?, ?, ?)')
                .bindOid(1, oid)
                .bindString(2, name)
                .bindInt(3, session)
                .executeStatement();
	}

	public function withTransaction<T>(callback: () -> T): T {
        db.executeSql("BEGIN");
        try {
            var result = callback();
            db.executeSql("COMMIT");
			return result;
        } catch (f: Fault) {
            db.executeSql("ROLLBACK");
            throw f.rethrow();
        } catch (d: Dynamic) {
			db.executeSql("ROLLBACK");
			throw d;
		}
    }

    public function getHierarchy(root: Oid, containment: String): Map<Oid, Noise> {
        var set: Map<Oid, Noise> = [root => Noise];
        for (child in getSetProperty(root, containment).getAll()) {
			for (key in getHierarchy(child, containment).keys()) {
				set[key] = true;
			}
        }
        return set;
    }
}

