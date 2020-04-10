package interfaces;

class AbstractTime implements ITime {
	public function millitime() return realtime() * 1000.0;
	public function hourstime() return realtime() / 3600.0;
	public function nanotime() return realtime() * 1e9;

	public function realtime(): Float throw "abstract method";
	public function formatTime(t: Float): String throw "abstract method";
}

interface ITime {
    /* Returns seconds since epoch */
    function realtime(): Float;

    function formatTime(t: Float): String;
}


