(function()
{
    "use strict";
	var authcookie;
	var tag = 0;
	
	G.IO =
	{
		RPC: function(message, success_cb)
		{
			message.tag = tag;
			tag++;
			
			var s = "> "+message.tag+" "+message.cmd;
			if (message.gcmd)
				s += "="+message.gcmd;
			console.log(s);
			
			$.ajax(G.CGI,
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
			IO.RPC({ cmd: "GetStatics" },
				function (data)
				{
					Database.ParseStatics(data);
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
			message.time = Database.GetServerTime();
			IO.RPC(message,
				function (data)
				{
    				if (data.changed)
    					Database.Synchronise(data);
    				if (success_cb)
    					success_cb(data);
				}
			);
		}
	};
}
)();


