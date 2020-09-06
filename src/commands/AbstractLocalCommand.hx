package commands;

import tink.CoreApi;

@await
class AbstractLocalCommand<Res> extends AbstractCommand<Res> {
	@async public override function callAsync(): Noise {
		callSync();
		return Noise;
	}

}

