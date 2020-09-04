package commands;

import interfaces.IRemoteClient;
import tink.CoreApi;
import utils.Fault;
import utils.Injectomatic.inject;

@await
class AbstractRemoteCommand<Req, Res> extends AbstractCommand<Req, Res> {
	var remoteClient = inject(IRemoteClient);

	@async override function callAsync(argv: Array<String>): Noise {
		parse(argv);
		var res: Res = @await remoteClient.call(argv);
		render(res);
		return Noise;
	}
}


