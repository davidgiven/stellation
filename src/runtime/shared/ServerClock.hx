package runtime.shared;

import interfaces.IClock;

class ServerClock implements IClock {
    var time = 0.0;

	public function new() {}

    public function setTime(serverTime: Float) {
        this.time = serverTime;
    }

    public function getTime() return time;
}

