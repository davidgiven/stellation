@file:Suppress("NOTHING_TO_INLINE")

package utils

import kotlin.reflect.KClass

const val COUNT = "_count"
const val SUCCESS = "_success"

class Parameters(var _map: Map<String, String> = emptyMap()): Iterable<String> {
    var count = 0

    init {
        count = _map[COUNT]?.toInt() ?: 0
    }

    inline val size: Int get() = getOrDefault(COUNT, 0)

    fun toMap(): Map<String, String> {
        if (count == 0) {
            _map -= COUNT
        } else {
            _map += COUNT to count.toString()
        }
        return _map
    }

    fun toList(): List<String> {
        var list: List<String> = emptyList()
        for (i in 0..(count - 1)) {
            val arg: String = get(i)
            list += arg
        }
        return list
    }

    override fun iterator(): Iterator<String> = toList().iterator()
}

inline fun <K> Parameters.clear(key: K) {
    _map -= key.toString()
}

inline operator fun <K, V> Parameters.set(key: K, value: V) {
    _map += key.toString() to value.toString()
}

@Suppress("UNCHECKED_CAST")
fun <K, V> Parameters.getOrNull(key: K, kclass: KClass<*>): V? {
    val value = _map[key.toString()]
    value ?: return null
    return when (kclass) {
        Char::class    -> value[0] as V
        Int::class     -> value.toInt() as V
        Short::class   -> value.toShort() as V
        Boolean::class -> value.toBoolean() as V
        Float::class   -> value.toFloat() as V
        Double::class  -> value.toDouble() as V
        String::class  -> value as V
        else           -> throw IllegalArgumentException()
    }
}

inline fun <K, reified V> Parameters.getOrNull(key: K): V? = getOrNull(key, V::class)

inline operator fun <K, reified V> Parameters.get(key: K): V = getOrNull(key)!!
inline fun <K, reified V> Parameters.getOrDefault(key: K, default: V): V = getOrNull(key) ?: default

inline fun <reified V> Parameters.add(value: V): Parameters {
    set(count++, value)
    return this
}

inline fun Parameters.setSuccess(success: Boolean) = set(SUCCESS, success)
inline fun Parameters.getSuccess(): Boolean = get(SUCCESS)

