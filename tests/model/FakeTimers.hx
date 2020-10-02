package model;

import interfaces.ITimers;
import utils.Oid;

class FakeTimers implements ITimers {
	public function new() {}

	public function setTimer(oid: Oid, method: String, expiry: Float) {}
	public function popOldestTimer(maxExpiry: Float): Null<Timer> { return null; }
}

