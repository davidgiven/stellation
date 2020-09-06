package client;

import haxe.Exception;
import haxe.Serializer;
import haxe.Unserializer;
import interfaces.ILogger.Logger.log;
import interfaces.IRemoteClient;
import interfaces.RPC;
import js.html.XMLHttpRequest;
import model.Syncer;
import tink.CoreApi;
import utils.Fault;
import utils.Injectomatic.inject;

@:tink
@await
class RemoteClient implements IRemoteClient {
	private var syncer = inject(Syncer);

	private var username: String;
	private var password: String;
	private var session = 0;

	public function new() {}

	public function setCredentials(username: String, password: String): Void {
		this.username = username;
		this.password = password;
	}

	@async public function call<Res>(argv: Array<String>): Res {
		var rpcReq: RpcRequest = {
			syncSession: session,
			argv: argv,
			username: username,
			password: password
		};

		var s = new Serializer();
		s.useCache = true;
		s.serialize(rpcReq);

		var response = @await Future.async((handler: Outcome<String, Error> -> Void) -> {
			var xhr = new XMLHttpRequest();
			xhr.onreadystatechange = () -> {
				if (xhr.readyState == XMLHttpRequest.DONE) {
					switch (xhr.status) {
						case 200:
							handler(Success(xhr.responseText));

						case 401:
							handler(Failure(tink.await.Error.fromAny(Fault.AUTH_FAILED)));

						default:
							var f = new Fault(NETWORK).withDetail("network error");
							handler(Failure(tink.await.Error.fromAny(f)));
					}
				}
			}
			xhr.open("POST", Configuration.SERVER_URL, true);
			xhr.send(s.toString());
		});

		var u = new Unserializer(response);
		u.setResolver(null);
		var rpcRes: RpcResponse = u.unserialize();
		if (rpcRes.fault != null) {
			throw new Fault(rpcRes.fault.domain)
					.withStatus(rpcRes.fault.status)
					.withDetail(rpcRes.fault.detail);
		}

		session = rpcRes.syncSession;
		if (rpcRes.syncData != null) {
			syncer.importSyncPacket(rpcRes.syncData);
		}
		return rpcRes.response;
	}
//        val sendMessage = ServerMessage()
//        sendMessage.setCommandInput(command.argv)
//        sendMessage.setUsername(username)
//        sendMessage.setPassword(password)
//        if (session != 0) {
//            sendMessage.setSyncSession(session)
//        }
//
//        val mailbox: Mailbox<ServerMessage> = Mailbox()
//        val xhr = XMLHttpRequest()
//        xhr.onreadystatechange = {
//            if (xhr.readyState == XMLHttpRequest.DONE) {
//                var receiveMessage: ServerMessage
//                val status = xhr.status.toInt()
//                try {
//                    when (status) {
//                        200 ->
//                            receiveMessage = ServerMessage(xhr.responseText)
//                        401 ->
//                            throwAuthenticationFailedException()
//                        else ->
//                            throw Fault(NETWORK).withStatus(status).withDetail("network error")
//                    }
//                } catch (f: Fault) {
//                    receiveMessage = ServerMessage()
//                    receiveMessage.setFault(f)
//                }
//                mailbox.post(receiveMessage)
//                kickScheduler()
//            }
//        }
//        xhr.open("POST", "http://localhost/~dg/cgi-bin/stellation.cgi", true)
//        xhr.send(sendMessage.serialise())
//
//        val receiveMessage = mailbox.wait()
//        if (receiveMessage.hasFault()) {
//            throw receiveMessage.getFault()
//        }
//
//        authenticator.setAuthenticatedPlayer(receiveMessage.getPlayerOid())
//        session = receiveMessage.getSyncSession()
//        syncer.importSyncPacket(receiveMessage.getSyncMessage())
//        clock.setTime(receiveMessage.getClock())
//        command.output = receiveMessage.getCommandOutput()
}
