package runtime.shared;

import haxe.io.Encoding;
import interfaces.ILogger;
import sys.io.File;
import sys.io.FileOutput;

class FileLogger implements ILogger {
	private var file: FileOutput;

	public function new(path: String) {
		file = File.append(path, false);
	}

	public function log(s: String): Void {
		file.writeString('${Sys.time()} $s\n', Encoding.UTF8);
		file.flush();
	}
}

