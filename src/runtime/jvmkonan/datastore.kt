package runtime.jvmkonan

import datastore.IDatabase
import datastore.Oid
import interfaces.IDatastore
import interfaces.SetProperty

class SqlDatastore(val database: IDatabase): IDatastore {
    override fun createObject(): Oid {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun destroyObject(oid: Oid) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun doesObjectExist(oid: Oid): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setIntProperty(oid: Oid, name: String, value: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getIntProperty(oid: Oid, name: String): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setLongProperty(oid: Oid, name: String, value: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getLongProperty(oid: Oid, name: String): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setRealProperty(oid: Oid, name: String, value: Double) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getRealProperty(oid: Oid, name: String): Double {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setStringProperty(oid: Oid, name: String, value: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getStringProperty(oid: Oid, name: String): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getSetProperty(oid: Oid, name: String): SetProperty {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
