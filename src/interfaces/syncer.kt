package interfaces

import utils.Oid
import utils.SyncMessage

interface ISyncer {
    fun exportSyncPacket(player: Oid, session: Int): SyncMessage
    fun importSyncPacket(sync: SyncMessage)

    fun listen(oid: Oid, property: String, callback: (Oid, String) -> Unit)
}
