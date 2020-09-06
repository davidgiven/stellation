package model;

import utils.Injectomatic.inject;
import tink.CoreApi;
import model.Properties;
import interfaces.IDatastore;
import utils.Oid;
using utils.ArrayTools;
using model.ObjectSetTools;

typedef SyncMessage = Map<Oid, Map<String, Dynamic>>;

@:tink
class Syncer {
	var universe = inject(SUniverse);
	var galaxy = inject(SGalaxy);
	var datastore = inject(IDatastore);
	var objectLoader = inject(ObjectLoader);

	public function new() {}

	public function exportSyncPacket(player: SPlayer, session: Int): SyncMessage {
		var p = new SyncMessage();

        /* TODO: it'd be nice to cache the visible object set every time the hierarchy
         * changes rather than calculating it every time; but it's not expensive and
         * this is reliable.
		 */
		var visibleObjects: Map<SThing, Noise> = [universe => Noise, galaxy => Noise];
		visibleObjects.addArray(galaxy.contents.getAll());
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
	}
}

