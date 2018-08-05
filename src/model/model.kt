package model

import interfaces.IAuthenticator
import interfaces.IDatastore
import utils.Oid
import utils.Fault
import utils.FaultDomain.INVALID_ARGUMENT
import utils.injection
import kotlin.reflect.KClass

fun throwObjectNotVisibleException(oid: Oid): Nothing =
        throw Fault(INVALID_ARGUMENT).withDetail("object $oid does not exist or is not visible")

fun throwDatabaseTypeMismatchException(oid: Oid, kind: String, desired: String): Nothing =
        throw Fault(INVALID_ARGUMENT).withDetail("expected $oid to be a $desired, but it was a $kind")

private typealias ClassMap = Map<String, (Model, Oid) -> SThing>

const val UNIVERSE_OID = 1
const val GOD_OID = 2

class Model {
    val datastore by injection<IDatastore>()
    val authenticator by injection<IAuthenticator>()

    private var objectCache = emptyMap<Oid, SThing>()

    private val classConstructors: ClassMap = emptyMap<String, (Model, Oid) -> SThing>()
            .registerClass(::SAntimatterDistillery)
            .registerClass(::SAsteroidMiner)
            .registerClass(::SCargo)
            .registerClass(::SFactory)
            .registerClass(::SFrame)
            .registerClass(::SGalaxy)
            .registerClass(::SHydroponicsPlant)
            .registerClass(::SJumpdrive)
            .registerClass(::SModule)
            .registerClass(::SPlayer)
            .registerClass(::SRAMCannon)
            .registerClass(::SRefinery)
            .registerClass(::SShip)
            .registerClass(::SStar)
            .registerClass(::STank)
            .registerClass(::SUniverse)
            .registerClass(::SWeapon)

    private inline fun <reified T : SThing> ClassMap.registerClass(noinline constructor: (Model, Oid) -> T): ClassMap =
            this + Pair(T::class.simpleName!!, constructor)

    fun initialiseProperties() {
        allProperties.values.forEach { datastore.createProperty(it.name, it.sqlType, it.isAggregate) }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : SThing> loadRawObject(oid: Oid, kclass: KClass<T>): T {
        if (!datastore.doesObjectExist(oid)) {
            throwObjectNotVisibleException(oid)
        }

        val kind = datastore.getStringProperty(oid, KIND.name)
        val instance: SThing = classConstructors.getValue(kind)(this, oid)
        if (kclass.isInstance(instance)) {
            return instance as T
        }
        throwDatabaseTypeMismatchException(oid, kind, kclass.simpleName!!)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : SThing> loadObject(oid: Oid, kclass: KClass<T>): T {
        var instance = objectCache.get(oid)
        if (instance == null) {
            instance = loadRawObject(oid, kclass)
            objectCache += oid to instance
        }

        if (kclass.isInstance(instance)) {
            return instance as T
        }
        throwDatabaseTypeMismatchException(
                oid, datastore.getStringProperty(oid, KIND.name), kclass.simpleName!!)
    }

    fun <T : SThing> createObject(klass: KClass<T>): T {
        val oid = datastore.createObject()
        datastore.setStringProperty(oid, KIND.name, klass.simpleName!!)
        return loadObject(oid, klass)
    }

    fun getUniverse(): SUniverse = loadObject(UNIVERSE_OID, SUniverse::class)
    fun getGod(): SPlayer = loadObject(GOD_OID, SPlayer::class)
}

fun <T : SThing> Oid.load(model: Model, klass: KClass<T>): T = model.loadObject(this, klass)
fun <T : SThing> Oid.loadRaw(model: Model, klass: KClass<T>): T = model.loadRawObject(this, klass)
