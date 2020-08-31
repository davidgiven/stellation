package model;

import utils.Injectomatic.inject;
import model.ObjectLoader;
import model.SUniverse;
import utest.Assert;

@:tink
class AbstractObjectsTest extends TestCase {
	@:calc var objectLoader = inject(ObjectLoader);

    function setup() {
    }

    function testObjectCreation() {
        var universe = objectLoader.createObject(SUniverse);
        Assert.same(1, universe.oid);
        Assert.same(SUniverse, universe.kind);
    }

//    @Test
//    fun objectCreationTest() {
//        val universe = model.createObject(SUniverse::class)
//        assertEquals(1, universe.oid)
//        assertEquals(SUniverse::class.simpleName, universe.kind)
//
//        val star = model.createObject(SStar::class)
//        assertEquals(2, star.oid)
//        assertEquals(SStar::class.simpleName, star.kind)
//    }

}

