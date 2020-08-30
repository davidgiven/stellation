package runtime.shared;

import interfaces.IDatastore;
import utest.Assert;
import utils.Injectomatic.inject;
using Lambda;
using runtime.shared.SetPropertyTools;

@:tink
class AbstractDatastoreTest extends TestCase {
	@:calc var datastore = inject(IDatastore);

	function setup() {
		for (t in ["INTEGER", "REAL", "TEXT"]) {
			datastore.createProperty(t.toLowerCase(), t, false);
		}

        datastore.createProperty(
                "oid",
                "INTEGER REFERENCES objects(oid) ON DELETE CASCADE",
                false);
        datastore.createProperty(
                "set",
                "INTEGER NOT NULL REFERENCES objects(oid) ON DELETE CASCADE",
                true);
	}

    function testObjectCreation() {
        var o1 = datastore.createObject();
		Assert.same(1, o1);

        var o2 = datastore.createObject();
		Assert.same(2, o2);
    }

    function testSetGetInt() {
        var o = datastore.createObject();
        datastore.setIntProperty(o, "integer", 5);
        var i = datastore.getIntProperty(o, "integer");
		Assert.same(5, i);
    }

    function testSetGetString() {
        var o = datastore.createObject();
        datastore.setStringProperty(o, "string", "fnord");
        var i = datastore.getStringProperty(o, "string");
		Assert.same("fnord", i);
    }

    function testHasProperty() {
        var o = datastore.createObject();
		Assert.isFalse(datastore.hasProperty(o, "integer"));

        datastore.setIntProperty(o, "integer", 5);
		Assert.isTrue(datastore.hasProperty(o, "integer"));
    }

	function testAddItemsToSet() {
		var o = datastore.createObject();
		var p = datastore.getSetProperty(o, "set");
		var c = [datastore.createObject(), datastore.createObject(), datastore.createObject()];
		for (item in c) {
			p.add(item);
		}

		Assert.same(c.toMap(), p.getAll().toMap());
	}

	function testRemoveItemsFromSet() {
		var o = datastore.createObject();
		var p = datastore.getSetProperty(o, "set");
		var c = [datastore.createObject(), datastore.createObject(), datastore.createObject()];
		for (item in c) {
			p.add(item);
		}

		p.remove(c[1]);
		Assert.same([c[0], c[2]].toMap(), p.getAll().toMap());
	}

	function testGetItemFromSet() {
		var o = datastore.createObject();
		var p = datastore.getSetProperty(o, "set");
		var c = [datastore.createObject(), datastore.createObject(), datastore.createObject()];
		for (item in c) {
			p.add(item);
		}
		
		while (true) {
			var s = p.getOne();
			if (s == null) {
				break;
			}

			Assert.isTrue(c.exists((it) -> s == it));
			p.remove(s);
			c.remove(s);
		}

		Assert.isTrue(c.empty());
	}

	function testClearSet() {
		var o = datastore.createObject();
		var p = datastore.getSetProperty(o, "set");
		var c = [datastore.createObject(), datastore.createObject(), datastore.createObject()];
		for (item in c) {
			p.add(item);
		}
		
		p.clear();

		Assert.isTrue(p.getAll().toMap().empty());
	}

	function testMultipleSetReferencesShareData() {
		var o = datastore.createObject();
		var p1 = datastore.getSetProperty(o, "set");
		var p2 = datastore.getSetProperty(o, "set");
		var c = [datastore.createObject(), datastore.createObject(), datastore.createObject()];
		for (item in c) {
			p1.add(item);
		}

		Assert.same(p1.getAll(), p2.getAll());
	}

	function testOidNulls() {
		var o = datastore.createObject();
		Assert.same(null, datastore.getOidProperty(o, "oid"));
		datastore.setOidProperty(o, "oid", o);
		Assert.same(o, datastore.getOidProperty(o, "oid"));
		datastore.setOidProperty(o, "oid", null);
		Assert.same(null, datastore.getOidProperty(o, "oid"));
	}

	function testHierarchy() {
		var o = datastore.createObject();
		var o1 = datastore.createObject();
		datastore.getSetProperty(o, "set").add(o1);
		var o2 = datastore.createObject();
		datastore.getSetProperty(o, "set").add(o2);
		var o21 = datastore.createObject();
		datastore.getSetProperty(o2, "set").add(o21);
		var o22 = datastore.createObject();
		datastore.getSetProperty(o2, "set").add(o22);

		Assert.same([o, o1, o2, o21, o22].toMap(), datastore.getHierarchy(o, "set"));
		Assert.same([o2, o21, o22].toMap(), datastore.getHierarchy(o2, "set"));
		Assert.same([o1].toMap(), datastore.getHierarchy(o1, "set"));
	}
}

