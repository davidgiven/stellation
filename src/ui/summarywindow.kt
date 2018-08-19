package ui

import interfaces.IClock
import interfaces.IUiElement
import model.Model
import model.currentPlayer
import utils.injection

class SummaryWindow : AbstractWindow() {
    private val model by injection<Model>()
    private val clock by injection<IClock>()

    override val mainClass = "summaryWindow"
    override val isResizable = false
    override val layout = "right-to-left"

    override fun createTitlebar(div: IUiElement) {
        div.addText("span", "Summary")
    }

    override fun createUserInterface(div: IUiElement) {
        val player = model.currentPlayer()

        div.run {
            classes += "scrollable"

            addNameViewer(player)
            addTimeViewer(clock::getTime)
        }
    }
}
