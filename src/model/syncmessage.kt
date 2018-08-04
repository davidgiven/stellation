package model

import interfaces.Oid
import utils.Message
import utils.set

class SyncMessage : Message() {
    fun addVisibleObject(oid: Oid) {
        set(oid, "")
    }

    fun getVisibleObjects(): Set<Oid> =
            toMap()
                    .flatMap {
                        val k = it.key.toIntOrNull()
                        if (k != null) {
                            listOf(k)
                        } else {
                            emptyList()
                        }
                    }.toSet()

    fun addProperty(oid: Oid, name: String, value: String) {
        set("$oid.$name", value)
    }
}
