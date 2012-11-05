(function()
{
    "use strict";
	var authcookie;
	var tag = 0;
	
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
			
			$.ajax(S.CGI,
					{
						data: "data=" + $.toJSON(message),
						dataType: "json",
						type: "POST",
						
						success: function (data, status)
						{
							console.log("< " + data.tag + " " + data.result);
							if (success_cb)
								success_cb(data);
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
			message.time = S.Database.GetServerTime();
			S.IO.RPC(message,
				function (data)
				{
    				if (data.changed)
    					S.Database.Synchronise(data);
    				if (success_cb)
    					success_cb(data);
				}
			);
		}
	};
}
)();


