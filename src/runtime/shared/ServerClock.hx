package runtime.shared;

import interfaces.IClock;
import utils.Fault;
import tink.CoreApi;

class ServerClock implements IClock {
    var time = 0.0;

	public function new() {}

    public function setTime(serverTime: Float) {
        this.time = serverTime;
    }

    public function getTime() {
		return time;
	}

	public function onTimeChanged(): Signal<Float> {
		throw Fault.UNIMPLEMENTED;
	}
}

