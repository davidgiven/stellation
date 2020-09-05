package model;

import utils.Oid;
import utils.Injectomatic.inject;
import utils.Random;
import interfaces.IDatastore;
import model.Properties;
import tink.CoreApi;
using Lambda;
using utils.ArrayTools;

@:remove
@:autoBuild(model.HasPropertiesMacro.build())
interface HasProperties {}

@:tink
class SThing implements HasProperties {
	public var oid: Oid;
	public var kind: Class<SThing>;

	public var datastore = inject(IDatastore);
	public var objectLoader = inject(ObjectLoader);
	@:lazy private var random = inject(Random);
	
	@:sproperty public var owner: Null<SThing>;
	@:sproperty public var location: Null<SThing>;
	@:sproperty public var contents: ObjectSet<SThing>;
	@:sproperty public var name: String;

	public function new() {}

	public function toString(): String {
		var n = name;
		if (n == null) {
			return '#$oid';
		} else {
			return '#$oid("$n")';
		}
	}

	@:generic
	public inline function findChild<T: SThing>(klass: Class<Dynamic>): Null<T> {
		var o: Dynamic = contents.getAll().find(o -> o.kind == klass);
		return o;
	}

	public function getContainingStar(): SStar {
		var loc: SThing = this;
		while (loc != null) {
			if (loc.kind == SStar) {
				return cast(loc, SStar);
			}
			loc = loc.location;
		}
		return null;
	}

	public function calculateHierarchicalContents(): Map<SThing, Noise> {
		return [
			for (oid in datastore.getHierarchy(oid, "contents").keys())
			objectLoader.loadObject(oid, SThing)
		].toMap();
	}
}

