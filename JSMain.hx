import js.Browser;
class JSMain {
    static function main() {
        var button = Browser.document.createButtonElement();
        button.textContent = "Click me!";
        button.onclick = function(event) {
            Browser.alert("Haxe is great");
        }
        Browser.document.body.appendChild(button);
    }
}

