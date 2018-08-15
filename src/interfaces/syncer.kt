package interfaces

import utils.Oid
import utils.SyncMessage

interface ISyncer {
    fun exportSyncPacket(player: Oid, session: Int): SyncMessage
    fun importSyncPacket(sync: SyncMessage)
}
