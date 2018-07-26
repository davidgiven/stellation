package utils

import kotlin.reflect.KClass

class InjectomaticException(message: String) : RuntimeException(message)

var injectomatic_bindings: Map<KClass<*>, Any?> = emptyMap()

fun resetBindingsForTest() {
    injectomatic_bindings = emptyMap()
}

fun checkBindingDoesNotExist(kclass: KClass<*>) {
    if (injectomatic_bindings.containsKey(kclass)) {
        throw InjectomaticException("binding for type ${kclass.simpleName} already exists")
    }
}

fun checkBindingExists(kclass: KClass<*>) {
    if (!injectomatic_bindings.containsKey(kclass)) {
        throw InjectomaticException("no binding for type ${kclass.simpleName} exists")
    }
}

inline fun <reified T> bind(o: T): T {
    val kclass = T::class
    checkBindingDoesNotExist(kclass)
    injectomatic_bindings += kclass to o
    return o
}

inline fun <reified T> inject(): T {
    val kclass = T::class
    checkBindingExists(kclass)
    return injectomatic_bindings.get(kclass) as T
}

inline fun <reified T> injection() = lazy { inject<T>() }
