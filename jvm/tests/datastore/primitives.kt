package datastore

import com.google.common.truth.Truth.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import shared.SStar
import shared.SUniverse
import shared.bind
import shared.create

class DatabaseTest {
    @Before
    fun setup() {
        openDatabase(":memory:")
        initialiseDatabase()
    }

    @After
    fun teardown() {
        closeDatabase()
    }

    @Test
    fun objectCreationTest() {
        val t1 = SUniverse().create()
        assertThat(t1.oid).isEqualTo(1)

        val t2 = SStar().create()
        assertThat(t2.oid).isEqualTo(2)
    }

    @Test
    fun propertySetGetTest() {
        var s = SStar().create()
        assertThat(s.kind).isEqualTo("SStar")

        s.name = "Foo"
        assertThat(s.name).isEqualTo("Foo")
        s.name = "Bar"
        assertThat(s.name).isEqualTo("Bar")

        s.brightness = 7.6
        assertThat(s.brightness).isEqualTo(7.6)

        s.asteroidsM = 42
        assertThat(s.asteroidsM).isEqualTo(42)
    }

    @Test
    fun objectSaveAndLoadTest() {
        var s1 = SStar().create()
        s1.name = "Foo"
        s1.brightness = 7.6
        s1.asteroidsM = 42

        var s2 = SStar().bind(s1.oid)
        assertThat(s2.name).isEqualTo("Foo")
        assertThat(s2.brightness).isEqualTo(7.6)
        assertThat(s2.asteroidsM).isEqualTo(42)
    }
}