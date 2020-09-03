package runtime.cpp;

import interfaces.IConsole;
import tink.CoreApi;

@await
class Console implements IConsole {
    public function new() {}

    @async public function println(s: String): Noise {
        trace(s);
        return Noise;
    }
}

