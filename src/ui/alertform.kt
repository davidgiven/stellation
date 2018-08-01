package ui

import interfaces.IUiElement

class AlertForm(val title: String, val message: String) : AbstractForm<Unit>() {
    override val isResizable = false
    override val mainClass = "alertWindow"

    override fun createTitlebar(div: IUiElement) {
        div.addText("span", title)
    }

    override fun createUserInterface(div: IUiElement) {
        div.addHBox {
            classes += "container"

            addElement("div") {
                classes = setOf("message")
                addText("span", message)
            }
        }
    }

    override fun createButtonBox(div: IUiElement) {
        var okButton = createOkButtonBox(div)

        okButton.onActivate(::okClicked)
    }

    private fun okClicked() {
        post(Unit)
    }
}
