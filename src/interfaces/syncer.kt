package interfaces

import utils.Oid
import utils.SyncMessage

interface ISyncer {
    fun exportSyncPacket(player: Oid, timestamp: Double): SyncMessage
    fun importSyncPacket(sync: SyncMessage)

    fun listen(oid: Oid, property: String, callback: (Oid, String) -> Unit)
}
