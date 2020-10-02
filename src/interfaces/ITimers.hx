package interfaces;

import utils.Oid;

typedef Timer = {
	oid: Oid,
	method: String,
	expiry: Float
};

interface ITimers {
	public function setTimer(oid: Oid, method: String, expiry: Float): Void;
	public function popOldestTimer(maxTime: Float): Null<Timer>;
}

