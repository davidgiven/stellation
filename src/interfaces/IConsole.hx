package interfaces;

import tink.CoreApi;

interface IConsole {
	public function println(s: String): Promise<Noise>;
}

