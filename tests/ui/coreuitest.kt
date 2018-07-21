package ui

import interfaces.IUiElement
import interfaces.IUiNode
import interfaces.IUiText
import runtime.jvm.JvmStubUi
import utils.hasRunnableJobs
import utils.schedule
import kotlin.test.Test
import kotlin.test.assertEquals

class CoreUiTest {
    val ui = JvmStubUi()

    @Test
    fun creationHierarchy() {
        var child0: IUiNode? = null
        var child1: IUiNode? = null
        var child2: IUiNode? = null

        val top = ui.newModal {
            child0 = addElement("child0")
            child1 = addElement("child1") {
                child2 = addElement("child2")
            }
        }

        assertEquals(listOf(child0!!, child1!!), top.children)
        assertEquals(listOf(child2!!), child1!!.children)
    }

    @Test
    fun childRemoval() {
        var child0: IUiNode? = null
        var child1: IUiNode? = null
        var child2: IUiNode? = null
        var child3: IUiNode? = null

        val top = ui.newModal {
            child0 = addElement("child0")
            child1 = addElement("child1")
            child2 = addElement("child2")
            child3 = addElement("child3")
        }

        assertEquals(listOf(child0!!, child1!!, child2!!, child3!!), top.children)
        top.removeChild(child1!!)
        assertEquals(listOf(child0!!, child2!!, child3!!), top.children)
        top.removeChild(child3!!)
        assertEquals(listOf(child0!!, child2!!), top.children)
        top.removeChild(child0!!)
        assertEquals(listOf(child2!!), top.children)
        top.removeChild(child0!!)
        assertEquals(listOf(child2!!), top.children)
        top.removeChild(child2!!)
        assertEquals(listOf(), top.children)
    }

    @Test
    fun elementsAndText() {
        var child0: IUiElement? = null
        var child1: IUiText? = null
        var child2: IUiElement? = null

        val top = ui.newModal {
            child0 = addElement("child0")
            child1 = addText("child1", "Hello, world!")
            child2 = addElement("child2")
        }

        assertEquals(listOf(child0!!, child1!!, child2!!), top.children)
    }

    @Test
    fun elementIds() {
        var child0: IUiElement? = null
        var child1: IUiText? = null
        var child2: IUiElement? = null
        var child3: IUiText? = null

        ui.newModal {
            child0 = addElement("child0", "id0")
            child1 = addText("child1", "id1", "Hello, world!")
            child2 = addElement("child2")
            child3 = addText("child3", "Hello, world!")
        }

        assertEquals("id0", child0!!.id)
        assertEquals("id1", child1!!.id)
        assertEquals(null, child2!!.id)
        assertEquals(null, child3!!.id)
    }

    @Test
    fun activation() {
        var child0: IUiElement? = null
        var activated = false

        ui.newModal {
            child0 = addElement("child0") {
                onActivate {
                    activated = true
                }
            }
        }

        assertEquals(false, activated)
        child0!!.activate()
        runRunnableJobs()
        assertEquals(true, activated)
    }

    @Test
    fun setGetAttribute() {
        val top = ui.newModal {
            set("foo", "bar")
        }

        assertEquals("bar", top["foo"])
    }

    @Test
    fun replaceAttribute() {
        val top = ui.newModal {
            set("foo", "bar")
        }

        top["foo"] = "baz"
        assertEquals("baz", top["foo"])
    }

    @Test
    fun clearAttribute() {
        val top = ui.newModal {
            set("foo", "bar")
        }

        top.clear("foo")
        assertEquals(null, top["foo"])
    }

    @Test
    fun fetchUnsetAttribute() {
        val top = ui.newModal {}

        assertEquals(null, top["foo"])
    }

    private fun runRunnableJobs() {
        while (hasRunnableJobs()) {
            schedule()
        }
    }
}
