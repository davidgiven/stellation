package interfaces;

import utils.Oid;
import utils.Fault;
import utils.FaultDomain;

typedef SyncMessage = Map<Oid, Map<String, Dynamic>>;

typedef RpcRequest = {
	syncSession: Int,
	argv: Array<String>,
	username: String,
	password: String,
};

typedef RpcResponse = {
	response: Dynamic,
	player: Oid,
	fault: SerialisedFault,
	syncSession: Int,
	syncData: SyncMessage,
};

