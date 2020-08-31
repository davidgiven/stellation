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
	@:sproperty public var contents: OidSet;
	@:sproperty public var name: String;
//    var kind by KIND
//    var owner by OWNER
//    var location by LOCATION
//    val contents by CONTENTS
//    var name by NAME

	public function new() {}

	
}

