package runtime.shared;

import interfaces.ILogger;

class TraceLogger implements ILogger {
	public function new() {}

	public function log(s: String): Void {
		trace(s);
	}
}

