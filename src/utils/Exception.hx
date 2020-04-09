package utils;

class Exception {
	var msg: String;

	public function new(msg: String) {
		this.msg = msg;
	}

	public function toString(): String {
		return '${Type.getClass(this)}: ${msg}';
	}
}

