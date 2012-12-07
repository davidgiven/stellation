(function()
{
    "use strict";
	var authcookie;
	var tag = 0;
	
	var servermessages =
	{
		AuthenticationFailure: "That username or password is not recognised.",
		InsufficientResources: "Insufficient resources are avzilable.",
		PlayerExists: "That username is already taken."
	};
	
	S.IO =
	{
		RPC: function(message, success_cb)
		{
			message.tag = tag;
			tag++;
			
			var s = "> "+message.tag+" "+message.cmd;
			if (message.gcmd)
				s += "="+message.gcmd;
			console.log(s);
			
			S.ShowLoading();
			$.ajax(S.CGI,
					{
						data: "data=" + $.toJSON(message),
						dataType: "json",
						type: "POST",
						
						success: function (data, status)
						{
							S.HideLoading();
							console.log("< " + data.tag + " " + data.result);
							if (success_cb)
								success_cb(data);
							
							if (data.result !== "OK")
								S.IO.ServerError(data);
						}
					}
				);
		},
		
		GetStatics: function(success_cb)
		{
			S.IO.RPC({ cmd: "GetStatics" },
				function (data)
				{
					S.Database.ParseStatics(data);
					success_cb();
				}
			);
		},
	
		SetCookie: function(cookie)
		{
			console.log("auth cookie is now "+cookie);
			authcookie = cookie;
		},
		
		GameCommand: function(message, success_cb)
		{
			message.cmd = "GameCommand";
			message.cookie = authcookie;
			S.IO.RPC(message,
				function (data)
				{
					if (data.time)
						S.SetServerTime(data.time);
    				if (data.changed)
    					S.Database.Synchronise(data);
    				if (success_cb)
    					success_cb(data);
				}
			);
		},
		
		ServerError: function (message)
		{
			var s = servermessages[message.result];
			if (!s)
				s = message.result;
			S.Dialogue(s);
		}
	};
}
)();


