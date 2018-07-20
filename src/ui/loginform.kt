package ui

import interfaces.IUi
import interfaces.IUiElement
import utils.get

data class LoginData(
        val canceled: Boolean,
        val username: String? = null,
        val password: String? = null
)

class LoginForm(ui: IUi = get()) : AbstractForm<LoginData>(ui) {
    lateinit var emailInput: IUiElement
    lateinit var passwordInput: IUiElement
    lateinit var okButton: IUiElement
    lateinit var cancelButton: IUiElement

    override fun createTitlebar(div: IUiElement) {
        div.addText("p", "Titlebar")
    }

    override fun createUserInterface(div: IUiElement) {
        div.addVBox {
            addText("p",
            """If you're an existing user, enter your email address and
                | password to log in.""".trimMargin())

            addElement("label") {
                addText("p", "Email address:")
                emailInput = addElement("input") {
                    set("type", "text")
                }
            }

            addElement("label") {
                addText("p", "Password:")
                passwordInput = addElement("input") {
                    set("type", "text")
                }
            }
        }
    }

    override fun createButtonBox(div: IUiElement) {
        var (okButton, cancelButton) = createYesNoButtonBox(div)
        this.okButton = okButton
        this.cancelButton = cancelButton

        okButton.onActivate {
            post(LoginData(false, emailInput["value"], passwordInput["value"]))
        }

        cancelButton.onActivate {
            post(LoginData(true))
        }
    }
}
