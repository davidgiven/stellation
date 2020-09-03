package model;

import model.Properties;

@:tink
class SUniverse extends SThing {
    @:sproperty public var galaxy: Null<SGalaxy>;
	@:sproperty public var players: ObjectSet<SPlayer>;
}

