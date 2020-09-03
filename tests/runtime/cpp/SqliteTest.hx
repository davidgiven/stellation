package runtime.cpp;

import runtime.cpp.Sqlite;
import utest.Assert;
import utils.Fault;
using utils.ArrayTools;

class SqliteTest extends TestCase {
    var db: SqliteDatabase;

    function setup() {
        db = null;
    }

    function teardown() {
        if (db != null) {
            db.close();
        }
    }
        
    function testOpenClose() {
        var fault: Fault = null;
        try {
            var db = Sqlite.open(":memory:");
            db.close();
        } catch (f: Fault) {
            fault = f;
        }
        Assert.isNull(fault);
    }

    function testPrepare() {
        var fault: Fault = null;
        try {
            db = Sqlite.open(":memory:");
            var stmt = db.sqlStatement("BEGIN");
        } catch (f: Fault) {
            fault = f;
        }
        Assert.isNull(fault);
    }

    function testExecute() {
        var fault: Fault = null;
        try {
            db = Sqlite.open(":memory:");
            db.executeSql("BEGIN");
        } catch (f: Fault) {
            fault = f;
        }
        Assert.isNull(fault);
    }

    function testQuery() {
        db = Sqlite.open(":memory:");
        var stmt = db.sqlStatement("SELECT ? AS result");
        stmt.bindInt(1, 42);
        var result = stmt.executeQuery().toArray();
        Assert.same(1, result.length);
        Assert.same(42, result[0]["result"].toInt());
    }

    function testOids() {
        db = Sqlite.open(":memory:");
        var stmt = db.sqlStatement("SELECT ? AS oid1, ? AS oid2");
        stmt.bindOid(1, null);
        stmt.bindOid(2, 42);
        var result = stmt.executeQuery().toArray();
        Assert.same(1, result.length);
        Assert.same(null, result[0]["oid1"].toOid());
        Assert.same(42, result[0]["oid2"].toOid());
    }

    function testStrings() {
        db = Sqlite.open(":memory:");
        var stmt = db.sqlStatement("SELECT ? AS s1, ? AS s2");
        stmt.bindString(1, "string1");
        stmt.bindString(2, "string2");
        var result = stmt.executeQuery().toArray();
        Assert.same(1, result.length);
        Assert.same("string1", result[0]["s1"].toString());
        Assert.same("string2", result[0]["s2"].toString());
    }

    function testCache() {
        db = Sqlite.open(":memory:");
        var stmt1 = db.sqlStatement("SELECT 1 AS result");
        Assert.same(1, stmt1.executeSimpleQuery()["result"].toInt());

        var stmt2 = db.sqlStatement("SELECT 1 AS result");
        Assert.same(1, stmt2.executeSimpleQuery()["result"].toInt());

        Assert.equals(stmt1, stmt2);
    }
}

