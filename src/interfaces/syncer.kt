package interfaces

import utils.Oid
import utils.SyncMessage

interface ISyncer {
    fun exportSyncPacket(player: Oid, timestamp: Double): SyncMessage
    fun importSyncPacket(sync: SyncMessage)
}
