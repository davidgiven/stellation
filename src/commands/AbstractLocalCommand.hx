package commands;

import tink.CoreApi;

@await
class AbstractLocalCommand<Req, Res> extends AbstractCommand<Req, Res> {
	@async public override function callAsync(argv: Array<String>): Noise {
		callSync(argv);
		return Noise;
	}

}

