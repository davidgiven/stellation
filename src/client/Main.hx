package client;
import js.Browser;
import utils.Injectomatic.inject;
import utils.Injectomatic.bind;
import interfaces.ITime;
import runtime.shared.Time;

class Main {
    static function main() {
        Console.start();
        Console.info("haxe main start");

        bind(ITime, new Time());
        bind(String, "foo");

        var button = Browser.document.createButtonElement();
        button.textContent = "Click me!";
        button.onclick = function(event) {
            Browser.alert("Haxe is great");
        }
        Browser.document.body.appendChild(button);
    }
}

// vim: ts=4 sw=4 et

