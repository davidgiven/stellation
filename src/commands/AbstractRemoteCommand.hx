package commands;

import interfaces.IRemoteClient;
import tink.CoreApi;
import utils.Fault;
import utils.Injectomatic.inject;

@await
class AbstractRemoteCommand<Res> extends AbstractCommand<Res> {
	var remoteClient = inject(IRemoteClient);

	@async override function callAsync(): Noise {
		parse();
		var res: Res = @await remoteClient.call(argv);
		render(res);
		return Noise;
	}
}


