package ui;

import interfaces.IUi;

typedef LoginData = {
	canceled: Bool,
	?username: String,
	?password: String
};

@:tink
@await
class LoginForm extends AbstractForm<LoginData> {
	private var emailInput: IUiElement;
	private var passwordInput: IUiElement;
	private var loginButton: IUiElement;
	private var registerButton: IUiElement;

	private var defaultUsername: String;
	private var defaultPassword: String;

	public function new(defaultUsername: String, defaultPassword: String) {
		super();

		this.mainClass = "loginWindow";
		this.defaultUsername = defaultUsername;
		this.defaultPassword = defaultPassword;
	}

    override function createTitlebar(div: IUiElement) {
		div.addNode(
			ui.newText("span", "Log in")
		);
    }

    override function createUserInterface(div: IUiElement) {
		div.addNode(
			ui.newVBox()
				.addNode(
					ui.newText("p", "STELLATION VII")
						.addClasses(["wide-text", "centred-text"])
				)
				.addNode(
					ui.newElement("p")
						.addClasses(["centred-text"])
						.addNode(
							ui.newText("span", "Very alpha edition")
						)
						.addNode(
							ui.newElement("br")
						)
						.addNode(
							ui.newText("span", "(c) 2000-2020 David Given")
						)
				)
				.addNode(
					ui.newText("p", "If you're an existing user, enter your email address and password to log in.")
				)
				.addNode(
					ui.newElement("div")
						.setClasses(["loginGrid"])
						.addNode(
							ui.newText("label", "Email address:")
						)
						.addNode(
							emailInput = ui.newElement("input")
								.setAttr("type", "email")
								.setAttr("required", "true")
								.setAttr("value", defaultUsername)
						)
						.addNode(
							ui.newText("label", "Password:")
						)
						.addNode(
							passwordInput = ui.newElement("input")
								.setAttr("type", "password")
								.setAttr("autocomplete", "current-password")
								.setAttr("required", "true")
								.setAttr("value", defaultPassword)
						)
			)
		);
	}

	override function createButtonBox(div: IUiElement) {
		var buttons = createYesNoButtonBox(div, "Log in", "Register");
		loginButton = buttons.yes;
		registerButton = buttons.no;

		loginButton.onActivate().handle(n -> {
			onCompletionTrigger.trigger({
				canceled: false,
				username: emailInput.getValue(),
				password: passwordInput.getValue()
			});
		});

		registerButton.onActivate().handle(n -> {
			onCompletionTrigger.trigger({
				canceled: true
			});
		});
	}
}

