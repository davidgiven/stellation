package runtime.shared

import interfaces.IDatastore
import interfaces.ISyncer
import interfaces.log
import model.Model
import model.SPlayer
import model.SThing
import model.Scope
import model.allProperties
import model.calculateVisibleObjects
import utils.Oid
import utils.SyncMessage
import utils.injection

class Syncer : ISyncer {
    private val datastore by injection<IDatastore>()
    private val model by injection<Model>()

    private val listeners = HashMap<Pair<Oid, String>, (Oid, String) -> Unit>()

    override fun importSyncPacket(sync: SyncMessage) {
        /* Remove any objects which have become invisible. */

        val visibleObjects = sync.getVisibleObjects()
        val oldObjects = datastore.getAllObjects()
        val deletedObjects = datastore.getAllObjects() - visibleObjects
        for (oid in deletedObjects) {
            datastore.destroyObject(oid)
        }
        for (oid in visibleObjects) {
            if (!datastore.doesObjectExist(oid)) {
                datastore.createObject(oid)
            }
        }

        /* Merge in changed properties. */

        for ((oid, name, value) in sync.getChangedProperties()) {
            val property = allProperties[name]!!
            property.deserialiseFromString(model, oid, value)
        }

        /* Call listeners. */


    }

    override fun exportSyncPacket(player: Oid, timestamp: Double): SyncMessage {
        val p = SyncMessage()

        val playerObj = model.loadObject(player, SPlayer::class)
        var visibleObjects =
                setOf(model.getUniverse(), model.getUniverse().galaxy!!) +
                        model.getUniverse().galaxy!!.contents + playerObj.calculateVisibleObjects()

        for (o in visibleObjects) {
            p.addVisibleObject(o.oid)
        }
        val changedProperties = datastore.getPropertiesChangedSince(
                visibleObjects.map { it.oid }, timestamp)
        for ((oid, propertyName) in changedProperties) {
            val property = allProperties[propertyName]!!
            val thing = model.loadObject(oid, SThing::class)
            val owner = thing.owner ?: model.getGod()
            if ((property.scope == Scope.LOCAL) || ((property.scope == Scope.PRIVATE) && (owner.oid == player))) {
                val serialised = property.serialiseToString(model, oid)
                p.addChangedProperty(oid, propertyName, serialised)
            }
        }
        return p
    }

    override fun listen(oid: Oid, property: String, callback: (Oid, String) -> Unit) {
        listeners.put(Pair(oid, property), callback)
    }
}
