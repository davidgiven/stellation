package interfaces

abstract class AbstractTime implements ITime {
	public function millitime() return realtime() * 1000.0;
	public function hourstime() return realtime() / 3600.0;
	public function nanotime() return realtime() * 1e9;
}

interface ITime {
    /* Returns seconds since epoch */
    function realtime(): Float;

    function formatTime(t: Double): Float;
}

