package runtime.cpp;

import interfaces.IConsole;
import tink.CoreApi;

class Console implements IConsole {
    public function new() {}

    public function println(s: String) {
        trace(s);
    }
}

