package model;

import utils.Injectomatic.inject;
import tink.CoreApi;
import model.Properties;
import interfaces.IDatastore;
import utils.Oid;
import interfaces.ILogger.Logger.log;
import interfaces.RPC;
using utils.ArrayTools;
using model.ObjectSetTools;
using interfaces.OidSetTools;

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
		visibleObjects.addMap(player.calculateVisibleObjects());
		player.visibleObjects.replaceAll([for (k in visibleObjects.keys()) k]);

		visibleObjects.addArray(universe.galaxy.contents.getAll());
		var changedProperties = datastore.getPropertiesChangedSince(
			[for (o => n in visibleObjects) o.oid], session);
			
		for (pair in changedProperties) {
			var oid = pair.a;
			var propertyName = pair.b;
			var property = objectLoader.findProperty(propertyName);
			var thing = objectLoader.loadObject(oid, SThing);
			var owner = thing.owner | if (null) objectLoader.findGod();
			var scope = property.scope;
			var visible = player.canSee(thing);
			if (((scope == Scope.LOCAL) && visible)
					|| ((scope == Scope.PRIVATE) && (owner == player))
					|| thing.hasGlobalVisibility(property)) {
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
			var kind = props[ObjectLoader.KIND];
			if (kind != null) {
				/* This property is being created. Set the kind now, so we can
				 * load it. */
				datastore.setStringProperty(oid, ObjectLoader.KIND, kind);
				props.remove(ObjectLoader.KIND);
			}

			var thing = objectLoader.loadObject(oid, SThing);
			for (propertyName => value in props) {
				var property = objectLoader.findProperty(propertyName);
				if (property != null) {
					property.setDynamicValue(thing, value);
				}
			}
		}

		/* Fire any listeners (only after the database has been updated). */

		for (oid => props in message) {
			var thing = objectLoader.loadObject(oid, SThing);
			for (propertyName => value in props) {
				var property = objectLoader.findProperty(propertyName);
				if (property != null) {
					thing.propertyChanged(property);
				}
			}
		}
	}
}

