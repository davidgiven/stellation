package interfaces;

import utils.Injectomatic.inject;

interface ILogger {
	public function log(s: String): Void;
}

class Logger {
	public static function log(s: String): Void {
		inject(ILogger).log(s);
	}
}
