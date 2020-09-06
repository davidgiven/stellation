package model;

import model.ObjectLoader;
import model.SUniverse;
import utest.Assert;
import utils.Fault;
import utils.FaultDomain;
import utils.Injectomatic.inject;
using interfaces.OidSetTools;
using model.ThingTools;
using utils.ArrayTools;

@:tink
class AbstractObjectsTest extends TestCase {
	@:calc var objectLoader = inject(ObjectLoader);

    function setup() {
        objectLoader.initialiseProperties();
    }

    function testObjectCreation() {
        var universe = objectLoader.createObject(SUniverse);
        Assert.same(1, universe.oid);
        Assert.same(SUniverse, universe.kind);

        var star = objectLoader.createObject(SStar);
        Assert.same(2, star.oid);
        Assert.same(SStar, star.kind);
    }

    function testPropertySetGet() {
        var s = objectLoader.createObject(SStar);
        Assert.same(SStar, s.kind);

        s.name = "Foo";
        Assert.same("Foo", s.name);
        s.name = "Bar";
        Assert.same("Bar", s.name);

        s.brightness = 7.6;
        Assert.floatEquals(7.6, s.brightness);
    }

    function testPropertySet() {
        var g = objectLoader.createObject(SGalaxy);
        var s1 = objectLoader.createObject(SStar).moveTo(g);
        var s2 = objectLoader.createObject(SStar).moveTo(g);
        var s3 = objectLoader.createObject(SStar).moveTo(g);

        Assert.same([s1, s2, s3].toMap(), g.contents.getAll().toMap());
    }

    function testObjectSaveAndLoad() {
        var s1 = objectLoader.createObject(SStar);
        s1.name = "Foo";
        s1.brightness = 7.6;

        var s2 = objectLoader.loadRawObject(s1.oid, SStar);
        Assert.notEquals(s2, s1);
        Assert.same("Foo", s2.name);
        Assert.floatEquals(7.6, s2.brightness);
    }

    function testObjectCache() {
        var g1 = objectLoader.createObject(SGalaxy);
        var g2 = objectLoader.loadObject(g1.oid, SGalaxy);
        Assert.equals(g2, g1);
    }

    function testObjectDoesNotExist() {
        try {
            objectLoader.loadRawObject(42, SStar);
            Assert.fail("exception not thrown");
        } catch (f: Fault) {
            Assert.same(INVALID_ARGUMENT, f.domain);
        }
    }

    function testReferences() {
        var u = objectLoader.createObject(SUniverse);
        var g = objectLoader.createObject(SGalaxy);

        Assert.notEquals(u.oid, g.oid);
        Assert.isNull(u.galaxy);
        u.galaxy = g;
        Assert.same(g.oid, u.galaxy.oid);

        u.galaxy = null;
        Assert.isNull(u.galaxy);
    }

    function testDowncast() {
        var f = objectLoader.createObject(SFactory);
        var m = objectLoader.loadRawObject(f.oid, SModule);

        Assert.same(m.kind, f.kind);
    }

    function testUpcast() {
        try {
            var m = objectLoader.createObject(SModule);
            objectLoader.loadRawObject(m.oid, SFactory);
            Assert.fail("exception not thrown");
        } catch (f: Fault) {
            Assert.same(INVALID_ARGUMENT, f.domain);
        }
    }
}

