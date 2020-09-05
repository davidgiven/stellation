package runtime.shared;

class Time {
	public function new() {}

	public function millitime() return realtime() * 1000.0;
	public function hourstime() return realtime() / 3600.0;
	public function nanotime() return realtime() * 1e9;

	public function realtime(): Float {
		#if js
			return Date.now().getTime() / 1000.0;
		#else
			return Sys.time();
		#end
	}
}

