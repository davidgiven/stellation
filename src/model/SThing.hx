package model;

import utils.Oid;
import utils.Injectomatic.inject;
import interfaces.IDatastore;
import model.Properties;
using Lambda;

@:remove
@:autoBuild(model.HasPropertiesMacro.build())
interface HasProperties {}

@:tink
class SThing implements HasProperties {
	public var oid: Oid;
	public var kind: Class<SThing>;

	public var datastore = inject(IDatastore);
	public var objectLoader = inject(ObjectLoader);
	
	@:sproperty public var owner: Null<SThing>;
	@:sproperty public var location: Null<SThing>;
	@:sproperty public var contents: ObjectSet<SThing>;
	@:sproperty public var name: String;

	public function new() {}
}

