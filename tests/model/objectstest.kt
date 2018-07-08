package model

import datastore.withSqlTransaction
import interfaces.IContext
import interfaces.context
import runtime.jvm.JvmDatabase
import runtime.shared.SqlDatastore
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class ObjectsTest {
    private val database get() = context.database!!
    private val datastore get() = context.datastore!!

    @BeforeTest
    fun setup() {
        context = object : IContext() {
            override val database = JvmDatabase()
            override val datastore = SqlDatastore(database)
        }

        database.openDatabase(":memory:")
        datastore.initialiseDatabase()
    }

    @AfterTest
    fun teardown() {
        database.closeDatabase()
    }

    @Test
    fun emptyTest() {
    }
}
