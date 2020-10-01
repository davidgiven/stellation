package model;

import utils.Oid;
import utils.Injectomatic.inject;
import utils.Random;
import interfaces.IDatastore;
import model.Properties;
import model.Properties.KIND;
import tink.CoreApi;
import utils.Fault;
using Lambda;
using utils.ArrayTools;

@:remove
@:autoBuild(model.HasPropertiesMacro.build())
interface HasProperties {}

@:tink
class SThing implements HasProperties {
	public var oid: Oid;
	public var kind: Class<SThing>;

	@:lazy var datastore = inject(IDatastore);
	@:lazy var objectLoader = inject(ObjectLoader);
	@:lazy var random = inject(Random);
	@:lazy var player = inject(SPlayer);
	
	@:sproperty public var owner: Null<SThing>;
	@:sproperty public var location: Null<SThing>;
	@:sproperty public var contents: ObjectSet<SThing>;
	@:sproperty public var name: String;

	var propertyListeners: Map<AbstractProperty, SignalTrigger<Noise>> = [];

	public function new() {}

	public function toString(): String {
		var n = name;
		if (n == null) {
			return '#$oid:$kind';
		} else {
			return '#$oid:$kind("$n")';
		}
	}

	@:generic
	public inline function as<T>(klass: Class<T>): T {
		if (Std.isOfType(this, klass)) {
			return cast(this);
		} else {
			return null;
		}
	}

	@:generic
	public inline function findChild<T: SThing>(klass: Class<Dynamic>): Null<T> {
		var o: Dynamic = contents.getAll().find(o -> o.kind == klass);
		return o;
	}

	public function init(): Void {}

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

    public function checkModificationAccess(?player: SPlayer): Void {
		if (player == null) {
			player = inject(SPlayer);
		}

        if (player.isGod()) {
            return;
        }
        if (!player.canSee(this)) {
			throw ObjectLoader.objectNotVisibleException(oid);
        }
        if (player != owner) {
			throw Fault.PERMISSION_DENIED;
        }
    }

	public function propertyChanged(property: AbstractProperty): Void {
		var trigger = propertyListeners[property];
		if (trigger != null) {
			trigger.trigger(Noise);
		}
	}
	
	public function onPropertyChanged(property: AbstractProperty): Signal<Noise> {
		var s = propertyListeners[property];
		if (s == null) {
			s = Signal.trigger();
			propertyListeners[property] = s;
		}
		return s.asSignal();
	}

	public function hasGlobalVisibility(property: AbstractProperty): Bool {
		if (property == KIND)
			return true;
		return false;
	}
}

