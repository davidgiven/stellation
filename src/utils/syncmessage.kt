package utils

class SyncMessage : Message {
    constructor(): super()
    constructor(serialised: String): super(serialised)

    fun addChangedProperty(oid: Oid, name: String, value: String) {
        set("$oid.$name", value)
    }

    fun getChangedProperties(): List<Triple<Oid, String, String>> =
            toMap()
                    .flatMap {
                        if (it.key.contains('.')) {
                            val oidString = it.key.substringBefore('.')
                            val name = it.key.substringAfter('.')
                            listOf(Triple(oidString.toInt(), name, it.value))
                        } else {
                            emptyList()
                        }
                    }
}
