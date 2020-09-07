package client;

import interfaces.IClock;
import utils.Fault;
import utils.Injectomatic.inject;
import runtime.shared.Time;
import tink.CoreApi;

@:tink
class ClientClock implements IClock {
	var time = inject(Time);
    var lastServerTime = 0.0;
	var lastRealTime = 0.0;
	var onTimeChangedTrigger: SignalTrigger<Float> = Signal.trigger();

	public function new() {
		lastServerTime = lastRealTime = time.realtime();
		@every(1.0) () -> onTimeChangedTrigger.trigger(getTime());
	}

    public function setTime(serverTime: Float) {
        lastServerTime = serverTime;
		lastRealTime = time.realtime();
		onTimeChangedTrigger.trigger(getTime());
    }

    public function getTime() {
		return (time.realtime() - lastRealTime) + lastServerTime;
	}

	public function onTimeChanged(): Signal<Float> {
		return onTimeChangedTrigger.asSignal();
	}
}


