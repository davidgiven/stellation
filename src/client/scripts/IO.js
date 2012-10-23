"use strict";

define(
	["jquery", "G", "Database"],
	function ($, G, Database)
	{
		var authcookie;
		
		var RPC = function(message, success_cb)
		{
			$.ajax(G.CGI,
					{
						data: "data=" + $.toJSON(message),
						dataType: "json",
						type: "POST",
						
						success: function (data, status)
						{
							if (success_cb)
								success_cb(data);
						}
					}
				);
		}
		
		return {
			RPC: RPC,
			
			GetStatics: function(success_cb)
			{
				RPC({ cmd: "GetStatics" },
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
				RPC(message,
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
);

