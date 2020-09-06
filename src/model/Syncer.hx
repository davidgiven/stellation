package model;

import utils.Injectomatic.inject;
import tink.CoreApi;
import model.Properties;
import interfaces.IDatastore;
import utils.Oid;
import interfaces.ILogger.Logger.log;
using utils.ArrayTools;
using model.ObjectSetTools;
using interfaces.OidSetTools;

typedef SyncMessage = Map<Oid, Map<String, Dynamic>>;

@:tink
class Syncer {
	var datastore = inject(IDatastore);
	var objectLoader = inject(ObjectLoader);

	public function new() {}

	public function exportSyncPacket(player: SPlayer, session: Int): SyncMessage {
		var p = new SyncMessage();

        /* TODO: it'd be nice to cache the visible object set every time the hierarchy
         * changes rather than calculating it every time; but it's not expensive and
         * this is reliable.
		 */
		var universe = objectLoader.findUniverse();
		var visibleObjects: Map<SThing, Noise> = [
			universe => Noise,
			universe.galaxy => Noise
		];
		visibleObjects.addArray(universe.galaxy.contents.getAll());
		visibleObjects.addMap(player.calculateVisibleObjects());
		player.visibleObjects.replaceAll([for (k in visibleObjects.keys()) k]);

		var changedProperties = datastore.getPropertiesChangedSince(
			[for (o => n in visibleObjects) o.oid], session);
			
		for (pair in changedProperties) {
			var oid = pair.a;
			var propertyName = pair.b;
			var property = objectLoader.findProperty(propertyName);
			var thing = objectLoader.loadObject(oid, SThing);
			var owner = thing.owner | if (null) objectLoader.findGod();
			if ((property.scope == Scope.LOCAL) || ((property.scope == Scope.PRIVATE) && (owner == player))) {
				var g = p[oid];
				if (g == null) {
					g = [];
					p[oid] = g;
				}
				g[propertyName] = property.getDynamicValue(thing);
				datastore.propertySeenBy(oid, propertyName, session);
			}
		}
        return p;
	}

	public function importSyncPacket(message: SyncMessage): Void {
		for (oid => props in message) {
			if (!datastore.doesObjectExist(oid)) {
				datastore.createSpecificObject(oid);
			}
			var kind = props["kind"];
			if (kind != null) {
				/* This property is being created. Set the kind now, so we can
				 * load it. */
				datastore.setStringProperty(oid, "kind", kind);
				props.remove("kind");
			}

			var thing = objectLoader.loadObject(oid, SThing);
			for (propertyName => value in props) {
				var property = objectLoader.findProperty(propertyName);
				if (property != null) {
					property.setDynamicValue(thing, value);
				}
			}
		}

//        /* Remove any objects which have become invisible. */
//
//        val changedProperties = sync.getChangedProperties()
//        val objects = changedProperties.map { it.first }.toSet()
//        for (oid in objects) {
//            if (!datastore.doesObjectExist(oid)) {
//                datastore.createObject(oid)
//            }
//        }
//
//        /* Merge in changed properties. */
//
//        for ((oid, name, value) in sync.getChangedProperties()) {
//            val property = allProperties[name]!!
//            property.deserialiseFromString(model, oid, value)
//        }
//
//        /* Fire listeners. (A separate loop to ensure that the database is fully updated before
//        * any listener runs. */
//
//        for ((oid, name, _) in sync.getChangedProperties()) {
//            ui.firePropertyChangedGlobalEvent(oid, name)
//        }
	}
}

