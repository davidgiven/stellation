package ui

import interfaces.IUiElement

class SummaryWindow : AbstractWindow() {
    override val mainClass = "summaryWindow"
    override val isResizable = false
    override val layout = "right-to-left"

    override fun createTitlebar(div: IUiElement) {
        div.addText("span", "Summary")
    }

    override fun createUserInterface(div: IUiElement) {
        div.run {
            classes += "scrollable"
        }
    }
}
