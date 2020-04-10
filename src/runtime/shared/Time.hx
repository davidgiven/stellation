package runtime.shared;

import interfaces.ITime.AbstractTime;

class Time extends AbstractTime {
	public function new() {}

	public override function realtime(): Float {
		#if js
			return Date.now().getTime() / 1000.0;
		#else
			return Sys.time();
		#end
	}
}

