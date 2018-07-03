package model

import com.google.common.truth.Truth.assertThat
import datastore.closeDatabase
import datastore.initialiseDatabase
import datastore.openDatabase
import org.junit.After
import org.junit.Before
import org.junit.Test

class ThingTest {
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
    fun moveToTest() {
        val g = createObject(SGalaxy::class)
        val s1 = createObject(SStar::class).moveTo(g)
        assertThat(s1.location).isEqualTo(g)
        assertThat(g.contents).containsExactly(s1)

        val s2 = createObject(SStar::class).moveTo(g)
        assertThat(s2.location).isEqualTo(g)
        assertThat(g.contents).containsExactly(s1, s2)

        s1.remove()
        assertThat(s1.location).isNull()
        assertThat(g.contents).containsExactly(s2)

        s2.remove()
        assertThat(s2.location).isNull()
        assertThat(g.contents).isEmpty()
    }

    @Test
    fun multipleRemoveTest() {
        val g = createObject(SGalaxy::class)
        val s = createObject(SStar::class).moveTo(g)
        s.remove()
        s.remove()
        assertThat(s.location).isNull()
        assertThat(g.contents).isEmpty()
    }

    @Test
    fun removeFromNowhereTest() {
        createObject(SGalaxy::class).remove()
    }
}
