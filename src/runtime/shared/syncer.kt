package runtime.shared

import interfaces.IDatastore
import interfaces.ISyncer
import interfaces.IUi
import interfaces.firePropertyChangedGlobalEvent
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
    private val ui by injection<IUi>()

    override fun importSyncPacket(sync: SyncMessage) {
        /* Remove any objects which have become invisible. */

        val changedProperties = sync.getChangedProperties()
        val objects = changedProperties.map { it.first }.toSet()
        for (oid in objects) {
            if (!datastore.doesObjectExist(oid)) {
                datastore.createObject(oid)
            }
        }

        /* Merge in changed properties. */

        for ((oid, name, value) in sync.getChangedProperties()) {
            val property = allProperties[name]!!
            property.deserialiseFromString(model, oid, value)
        }

        /* Fire listeners. (A separate loop to ensure that the database is fully updated before
        * any listener runs. */

        for ((oid, name, _) in sync.getChangedProperties()) {
            ui.firePropertyChangedGlobalEvent(oid, name)
        }
    }

    override fun exportSyncPacket(player: Oid, session: Int): SyncMessage {
        val p = SyncMessage()

        val playerObj = model.loadObject(player, SPlayer::class)

        // TODO: it'd be nice to cache the visible object set every time the hierarchy
        // changes rather than calculating it every time; but it's not expensive and
        // this is reliable.
        var visibleObjects =
                setOf(model.getUniverse(), model.getUniverse().galaxy!!) +
                        model.getUniverse().galaxy!!.contents + playerObj.calculateVisibleObjects()
        playerObj.visible_objects.replaceAll(visibleObjects)

        val changedProperties = datastore.getPropertiesChangedSince(
                visibleObjects.map { it.oid }, session)
        for ((oid, propertyName) in changedProperties) {
            val property = allProperties[propertyName]!!
            val thing = model.loadObject(oid, SThing::class)
            val owner = thing.owner ?: model.getGod()
            if ((property.scope == Scope.LOCAL) || ((property.scope == Scope.PRIVATE) && (owner.oid == player))) {
                val serialised = property.serialiseToString(model, oid)
                p.addChangedProperty(oid, propertyName, serialised)
                datastore.propertySeenBy(oid, propertyName, session)
            }
        }
        return p
    }
}
