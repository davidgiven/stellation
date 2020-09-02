package runtime.cpp;

import cpp.Char;
import cpp.ConstPointer;
import cpp.Finalizable;
import cpp.Pointer;
import cpp.Reference;
import utils.Fault;
import utils.FaultDomain;
import utils.Oid;
import haxe.ds.Vector;

@:include("sqlite3.h")
@:native("sqlite3")
extern class RawSqlite {}

@:include("sqlite3.h")
@:native("sqlite3_stmt")
extern class RawSqliteStmt {}

@:include("sqlite3.h")
@:native("sqlite3_value")
extern class RawSqliteValue {}

@:include("sqlite3.h")
class SqliteValue extends Finalizable {
    private var value: Pointer<RawSqliteValue>;

    public function new(value: Pointer<RawSqliteValue>) {
        this.value = untyped __cpp__("sqlite3_value_dup({0})", value);
        super();
    }

    public override function finalize() {
        untyped __cpp__("sqlite3_value_free({0})", value);
        super.finalize();
    }

    public inline function isNull(): Bool { return untyped __cpp__("sqlite3_value_type({0}) == SQLITE_NULL", value); }
    public inline function toInt(): Int { return untyped __cpp__("sqlite3_value_int({0})", value); }
    public inline function toFloat(): Float { return untyped __cpp__("sqlite3_value_double({0})", value); }
    public inline function toString(): String { return untyped __cpp__("(const char*)sqlite3_value_text({0})", value); }

    public inline function toOid(): Null<Oid> {
        return isNull() ? null : toInt();
    }
}

@:include("sqlite3.h")
class SqliteStatement extends Finalizable {
    private var db: Pointer<RawSqlite>;
    private var stmt: Pointer<RawSqliteStmt>;

    public function new(db: Pointer<RawSqlite>, stmt: Pointer<RawSqliteStmt>) {
        super();
        this.db = db;
        this.stmt = stmt;
    }

    public override function finalize() {
        untyped __cpp__("sqlite3_finalize({0})", stmt);
        super.finalize();
    }

    public function reset(): SqliteStatement {
        untyped __cpp__("sqlite3_reset({0})", stmt);
        return this;
    }

    public function bindInt(index: Int, value: Int): SqliteStatement {
        untyped __cpp__("sqlite3_bind_int({0}, {1}, {2})", stmt, index, value);
        return this;
    }

    public function bindFloat(index: Int, value: Float): SqliteStatement {
        untyped __cpp__("sqlite3_bind_double({0}, {1}, {2})", stmt, index, value);
        return this;
    }

    public function bindString(index: Int, value: String): SqliteStatement {
        untyped __cpp__("sqlite3_bind_text({0}, {1}, {2}, -1, SQLITE_TRANSIENT)", stmt, index, value);
        return this;
    }

    public function bindOid(index: Int, value: Null<Oid>): SqliteStatement {
        if (value == null) {
            untyped __cpp__("sqlite3_bind_null({0}, {1})", stmt, index);
        } else {
            untyped __cpp__("sqlite3_bind_int({0}, {1}, {2})", stmt, index, value);
        }
        return this;
    }

    public function executeStatement(): Void {
        var e: Int = untyped __cpp__("sqlite3_step({0})", stmt);
        if (e != Sqlite.DONE) {
            throw Sqlite.sqlException('Statement execution error $e ${Sqlite.sqliteError(db)}');
        }
    }

    public function executeQuery(): Iterator<Map<String, SqliteValue>> {
        var columnCount = untyped __cpp__("sqlite3_column_count({0})", stmt);
        var columnNames = new Vector<String>(columnCount);

        for (i in 0...columnCount) {
            columnNames[i] = untyped __cpp__("sqlite3_column_name({0}, {1})", stmt, i);
        }

        var status = untyped __cpp__("sqlite3_step({0})", stmt);
        return {
            hasNext: () -> status == Sqlite.ROW,
            next: () -> {
                switch (status) {
                    case Sqlite.DONE:
                        return null;

                    case Sqlite.ROW:
                        trace("row", status);
                        var data = new Map<String, SqliteValue>();
                        for (i in 0...columnCount) {
                            data[columnNames[i]] = new SqliteValue(
                                untyped __cpp__("sqlite3_column_value({0}, {1})", stmt, i)
                            );
                        }
                        status = untyped __cpp__("sqlite3_step({0})", stmt);
                        return data;

                    default:
                        throw Sqlite.sqlException("Cursor error $status");
                }
            }
        };
    }

    public function executeSimpleQuery(): Map<String, SqliteValue> {
        return executeQuery().next();
    }
}

@:include("sqlite3.h")
class SqliteDatabase extends Finalizable {
    private var db: Pointer<RawSqlite>;
    private var cache = new Map<String, SqliteStatement>();

    public function new(db: Pointer<RawSqlite>) {
        super();
        this.db = db;
    }

    public function close(): Void {
        untyped __cpp__("sqlite3_close({0})", db);
        db = null;
    }

    public override function finalize() {
        close();
        super.finalize();
    }

    public function sqlStatement(sql: String): SqliteStatement {
        var stmt = cache[sql];
        if (stmt == null) {
            untyped __cpp__("const char* rawtail;");
            untyped __cpp__("sqlite3_stmt* rawstmt;");
            var i: Int = untyped __cpp__("sqlite3_prepare_v2({0}, {1}, -1, &rawstmt, &rawtail)", db, sql);
            if (i != 0) {
                throw Sqlite.sqlException('Error preparing statement: ${Sqlite.sqliteError(db)}');
            }

            var tail: String = untyped __cpp__("rawtail");
            if (tail != "") {
                throw Sqlite.sqlException('Some of statement ignored: "${tail}"');
            }

            stmt = new SqliteStatement(db, untyped __cpp__("rawstmt"));
            cache[sql] = stmt;
        } else {
            stmt.reset();
        }
        return stmt;
    }

    public function executeSql(sql: String): Void {
        sqlStatement(sql).executeStatement();
    }
}

@:include("sqlite3.h")
@:buildXml('<target id="haxe"><lib name="-lsqlite3"/></target>')
class Sqlite {
    public static final DONE = untyped __cpp__("SQLITE_DONE");
    public static final ROW = untyped __cpp__("SQLITE_ROW");

    public static function open(path: String): SqliteDatabase {
        untyped __cpp__("sqlite3* rawdb");
        var i: Int = untyped __cpp__("sqlite3_open({0}, &rawdb)", path);
        var db: Pointer<RawSqlite> = untyped __cpp__("rawdb");
        if (i != 0) {
            throw sqlException('Cannot open db: ${sqliteError(db)}');
        }

        return new SqliteDatabase(db);
    }

    public static function sqliteError(db: Pointer<RawSqlite>): String {
        return untyped __cpp__("sqlite3_errmsg({0})", db);
    }
    
    public static function sqlException(s: String) {
        return new Fault(INTERNAL).withDetail(s);
    }
}

