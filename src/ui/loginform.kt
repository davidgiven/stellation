package ui

import interfaces.IUiElement

data class LoginData(
        val canceled: Boolean,
        val username: String? = null,
        val password: String? = null
)

class LoginForm : AbstractForm<LoginData>() {
    lateinit var emailInput: IUiElement
    lateinit var passwordInput: IUiElement
    lateinit var loginButton: IUiElement
    lateinit var registerButton: IUiElement

    override val isResizable = false
    override val mainClass = "loginWindow"

    override fun createTitlebar(div: IUiElement) {
        div.addText("span", "Log in")
    }

    override fun createUserInterface(div: IUiElement) {
        div.addVBox {
            addText("p", "STELLATION VI") {
                classes += setOf("wide-text", "centred-text")
            }
            addElement("p") {
                classes += setOf("centred-text")
                addText("span", "Very alpha edition")
                addElement("br")
                addText("span", "(c) 2000-2018 David Given")
            }

            addText(
                    "p",
                    """If you're an existing user, enter your email address and
                | password to log in.""".trimMargin())

            addElement("div") {
                classes = setOf("loginGrid")

                addText("label", "Email address:")
                emailInput = addElement("input") {
                    set("type", "email")
                    set("required", "true")
                }

                addText("label", "Password:")
                passwordInput = addElement("input") {
                    set("type", "password")
                    set("autocomplete", "current-password")
                    set("required", "true")
                    onActivate(::okClicked)
                }
            }
        }

        emailInput.onActivate { passwordInput.focus() }
    }

    override fun createButtonBox(div: IUiElement) {
        var (loginButton, registerButton) = createYesNoButtonBox(div, "Log in", "Register")
        this.loginButton = loginButton
        this.registerButton = registerButton

        loginButton.onActivate(::okClicked)
        registerButton.onActivate(::cancelClicked)
    }

    private fun okClicked() {
        post(LoginData(false, emailInput["value"], passwordInput["value"]))
    }

    private fun cancelClicked() {
        post(LoginData(true))
    }
}
